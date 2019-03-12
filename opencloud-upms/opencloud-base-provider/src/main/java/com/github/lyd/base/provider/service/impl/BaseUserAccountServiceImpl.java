package com.github.lyd.base.provider.service.impl;

import com.github.lyd.auth.client.constants.AuthConstants;
import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.model.BaseUserAccountDto;
import com.github.lyd.base.client.model.BaseUserDto;
import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.base.client.model.entity.BaseUserAccount;
import com.github.lyd.base.client.model.entity.BaseUserAccountLogs;
import com.github.lyd.base.provider.mapper.BaaeUserAccountLogsMapper;
import com.github.lyd.base.provider.mapper.BaseUserAccountMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseRoleService;
import com.github.lyd.base.provider.service.BaseUserAccountService;
import com.github.lyd.base.provider.service.BaseUserService;
import com.github.lyd.common.constants.CommonConstants;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.github.lyd.common.utils.RandomValueUtils;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.common.utils.WebUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserAccountServiceImpl implements BaseUserAccountService {

    @Autowired
    private BaseUserAccountMapper baseUserAccountMapper;
    @Autowired
    private BaaeUserAccountLogsMapper systemUserAccountLogsMapper;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BaseRoleService roleService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    public static final String ROLE_PRIFIX = "ROLE_";

    /**
     * 添加系统用户
     *
     * @param profileDto
     * @return
     */
    @Override
    public Long register(BaseUserDto profileDto) {
        if (profileDto == null) {
            return null;
        }
        if (StringUtils.isBlank(profileDto.getUserName())) {
            throw new OpenAlertException("用户名不能为空!");
        }
        if (StringUtils.isBlank(profileDto.getPassword())) {
            throw new OpenAlertException("密码不能为空!");
        }
        BaseUser saved = baseUserService.getProfile(profileDto.getUserName());
        if (saved != null) {
            // 已注册
            throw new OpenAlertException("用户名已被占用!");
        }
        //加密
        String encodePassword = passwordEncoder.encode(profileDto.getPassword());
        if (StringUtils.isBlank(profileDto.getNickName())) {
            profileDto.setNickName(profileDto.getUserName());
        }
        profileDto.setCreateTime(new Date());
        profileDto.setUpdateTime(profileDto.getCreateTime());
        profileDto.setRegisterTime(profileDto.getCreateTime());
        //保存系统用户信息
        Long userId = baseUserService.addProfile(profileDto);
        //默认注册用户名账户
        Long accountId = this.registerUsernameAccount(userId, profileDto.getUserName(), encodePassword);
        if (accountId != null && StringUtils.isNotBlank(profileDto.getEmail())) {
            //注册email账号登陆
            this.registerEmailAccount(userId, profileDto.getEmail(), encodePassword);
        }
        if (accountId != null && StringUtils.isNotBlank(profileDto.getMobile())) {
            //注册手机号账号登陆
            this.registerMobileAccount(userId, profileDto.getMobile(), encodePassword);
        }
        return userId;
    }

    /**
     * 绑定其他账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @Override
    public Long register(String account, String password, String accountType) {
        if (isExist(account, accountType)) {
            return null;
        }
        BaseUserDto user = new BaseUserDto();
        String name = accountType + "_" + RandomValueUtils.randomAlphanumeric(16);
        user.setPassword(password);
        user.setNickName(name);
        user.setUserName(name);
        user.setUserType(BaseConstants.USER_TYPE_PLATFORM);
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        user.setRegisterTime(user.getCreateTime());
        user.setStatus(BaseConstants.USER_STATE_NORMAL);
        Long userId = baseUserService.addProfile(user);
        //加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        BaseUserAccount systemAccount = new BaseUserAccount(userId, account, encodePassword, accountType);
        baseUserAccountMapper.insertSelective(systemAccount);
        return userId;
    }

    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public BaseUserAccountDto login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> headers = WebUtils.getHttpHeaders(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String thirdParty = headers.get(AuthConstants.HEADER_X_THIRDPARTY_LOGIN);
        BaseUserAccount systemAccount = null;
        // 账号返回数据
        BaseUserAccountDto systemAccountDto = null;
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        if (StringUtils.isNotBlank(thirdParty)) {
            // 第三方登录
            Example example = builder.criteria()
                    .andEqualTo("account", account)
                    .andEqualTo("accountType", thirdParty)
                    .end().build();
            systemAccount = baseUserAccountMapper.selectOneByExample(example);
        } else {
            // 非第三方登录, 账号密码方式登陆
            //用户名登录
            Example example = builder.criteria()
                    .andEqualTo("account", account)
                    .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_USERNAME)
                    .end().build();
            systemAccount = baseUserAccountMapper.selectOneByExample(example);

            // 手机号登陆
            if (systemAccount == null && StringUtils.matchMobile(account)) {
                // 用户名登陆
                //强制清空
                example.clear();
                //  尝试手机号登录
                example = builder.criteria()
                        .andEqualTo("account", account)
                        .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_MOBILE)
                        .end().build();
                systemAccount = baseUserAccountMapper.selectOneByExample(example);
            }

            // 邮箱登陆
            if (systemAccount == null && StringUtils.matchEmail(account)) {
                //强制清空
                example.clear();
                //  尝试邮箱登录
                example = builder.criteria()
                        .andEqualTo("account", account)
                        .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_EMAIL)
                        .end().build();
                systemAccount = baseUserAccountMapper.selectOneByExample(example);
            }
        }

        // 获取用户详细信息
        if (systemAccount != null) {
            // 用户权限列表
            List<OpenGrantedAuthority> authorities = Lists.newArrayList();
            // 用户角色列表
            List<Map> roles = Lists.newArrayList();
            List<BaseRole> rolesList = roleService.getUserRoles(systemAccount.getUserId());
            if (rolesList != null) {
                for (BaseRole role : rolesList) {
                    Map roleMap = Maps.newHashMap();
                    roleMap.put("roleId", role.getRoleId());
                    roleMap.put("roleCode", role.getRoleCode());
                    roleMap.put("roleName", role.getRoleName());
                    // 用户角色详情
                    roles.add(roleMap);
                    // 加入角色标识
                    OpenGrantedAuthority authority = new OpenGrantedAuthority(ROLE_PRIFIX + role.getRoleCode());
                    authority.setOwner("role");
                    authorities.add(authority);
                }
            }

            //查询系统用户资料
            BaseUser baseUser = baseUserService.getProfile(systemAccount.getUserId());

            // 加入用户权限
            List<OpenGrantedAuthority> userGrantedAuthority = baseAuthorityService.findUserGrantedAuthority(systemAccount.getUserId(),  CommonConstants.ROOT.equals(baseUser.getUserName()));
            if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
                authorities.addAll(userGrantedAuthority);
            }
            BaseUserDto userProfile = new BaseUserDto();
            BeanUtils.copyProperties(baseUser, userProfile);

            //设置用户资料,权限信息
            userProfile.setAuthorities(authorities);
            userProfile.setRoles(roles);
            systemAccountDto = new BaseUserAccountDto();
            BeanUtils.copyProperties(systemAccount, systemAccountDto);
            systemAccountDto.setUserProfile(userProfile);
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    BaseUserAccountLogs log = new BaseUserAccountLogs();
                    log.setUserId(systemAccount.getUserId());
                    log.setAccount(systemAccount.getAccount());
                    log.setAccountId(String.valueOf(systemAccount.getAccountId()));
                    log.setAccountType(systemAccount.getAccountType());
                    log.setLoginIp(WebUtils.getIpAddr(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    addLoginLog(log);
                }
            } catch (Exception e) {
                log.error("添加登录日志失败:{}",e);
            }
        }
        return systemAccountDto;
    }

    /**
     * 注册系统用户名账户
     *
     * @param userId
     * @param username
     * @param password
     */
    @Override
    public Long registerUsernameAccount(Long userId, String username, String password) {
        if (isExist(userId, username, BaseConstants.USER_ACCOUNT_TYPE_USERNAME)) {
            //已经注册
            return null;
        }
        BaseUserAccount systemAccount = new BaseUserAccount(userId, username, password, BaseConstants.USER_ACCOUNT_TYPE_USERNAME);
        baseUserAccountMapper.insertSelective(systemAccount);
        return systemAccount.getAccountId();
    }

    /**
     * 注册email账号
     *
     * @param userId
     * @param email
     * @param password
     */
    @Override
    public Long registerEmailAccount(Long userId, String email, String password) {
        if (!StringUtils.matchEmail(email)) {
            return null;
        }
        if (isExist(userId, email, BaseConstants.USER_ACCOUNT_TYPE_EMAIL)) {
            //已经注册
            return null;
        }
        BaseUserAccount systemAccount = new BaseUserAccount(userId, email, password, BaseConstants.USER_ACCOUNT_TYPE_EMAIL);
        baseUserAccountMapper.insertSelective(systemAccount);
        return systemAccount.getAccountId();
    }


    /**
     * 注册手机账号
     *
     * @param userId
     * @param mobile
     * @param password
     */
    @Override
    public Long registerMobileAccount(Long userId, String mobile, String password) {
        if (!StringUtils.matchMobile(mobile)) {
            return null;
        }
        if (isExist(userId, mobile, BaseConstants.USER_ACCOUNT_TYPE_MOBILE)) {
            //已经注册
            return null;
        }
        BaseUserAccount systemAccount = new BaseUserAccount(userId, mobile, password, BaseConstants.USER_ACCOUNT_TYPE_MOBILE);
        baseUserAccountMapper.insertSelective(systemAccount);
        return systemAccount.getAccountId();
    }


    /**
     * 更新系统用户密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public void resetPassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return;
        }
        BaseUser userProfile = baseUserService.getProfile(userId);
        if (userProfile == null) {
            throw new OpenAlertException("用户信息不存在!");
        }
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId)
                .andEqualTo("account", userProfile.getUserName())
                .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_USERNAME)
                .end().build();
        BaseUserAccount systemAccount = baseUserAccountMapper.selectOneByExample(example);
        if (systemAccount == null) {
            return;
        }
        String oldPasswordEncoder = passwordEncoder.encode(oldPassword);
        if (!passwordEncoder.matches(systemAccount.getPassword(), oldPasswordEncoder)) {
            throw new OpenAlertException("原密码错误!");
        }
        systemAccount.setPassword(passwordEncoder.encode(newPassword));
        baseUserAccountMapper.updateByPrimaryKey(systemAccount);
    }

    /**
     * 更新系统用户登录Ip
     *
     * @param log
     */
    @Override
    public void addLoginLog(BaseUserAccountLogs log) {
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccountLogs.class);
        Example example = builder.criteria().andEqualTo("userId", log.getUserId()).end().build();
        int count = systemUserAccountLogsMapper.selectCountByExample(example);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        systemUserAccountLogsMapper.insertSelective(log);
    }

    /**
     * 检查是否已注册账号
     *
     * @param userId
     * @param account
     * @param accountType
     * @return
     */
    @Override
    public Boolean isExist(Long userId, String account, String accountType) {
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId)
                .andEqualTo("account", account)
                .andEqualTo("accountType", accountType)
                .end().build();
        int count = baseUserAccountMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    @Override
    public Boolean isExist(String account, String accountType) {
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        Example example = builder.criteria()
                .andEqualTo("account", account)
                .andEqualTo("accountType", accountType)
                .end().build();
        int count = baseUserAccountMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 解绑email账号
     *
     * @param userId
     * @param email
     * @return
     */
    @Override
    public void removeEmailAccount(Long userId, String email) {
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId)
                .andEqualTo("account", email)
                .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_EMAIL)
                .end().build();
        baseUserAccountMapper.deleteByExample(example);
    }

    /**
     * 解绑手机账号
     *
     * @param userId
     * @param mobile
     * @return
     */
    @Override
    public void removeMobileAccount(Long userId, String mobile) {
        ExampleBuilder builder = new ExampleBuilder(BaseUserAccount.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId)
                .andEqualTo("account", mobile)
                .andEqualTo("accountType", BaseConstants.USER_ACCOUNT_TYPE_MOBILE)
                .end().build();
        baseUserAccountMapper.deleteByExample(example);
    }

}
