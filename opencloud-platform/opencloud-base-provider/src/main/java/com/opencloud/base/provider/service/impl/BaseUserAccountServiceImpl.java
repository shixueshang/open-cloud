package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.auth.client.constants.AuthConstants;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.BaseUserAccountDto;
import com.opencloud.base.client.model.BaseUserDto;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.model.entity.BaseUserAccount;
import com.opencloud.base.client.model.entity.BaseUserAccountLogs;
import com.opencloud.base.provider.mapper.BaseUserAccountLogsMapper;
import com.opencloud.base.provider.mapper.BaseUserAccountMapper;
import com.opencloud.base.provider.service.BaseUserAccountService;
import com.opencloud.base.provider.service.BaseUserService;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.utils.RandomValueUtils;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserAccountServiceImpl extends BaseServiceImpl<BaseUserAccountMapper, BaseUserAccount> implements BaseUserAccountService {

    @Autowired
    private BaseUserAccountMapper baseUserAccountMapper;
    @Autowired
    private BaseUserAccountLogsMapper systemUserAccountLogsMapper;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;


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
        BaseUser saved = baseUserService.getUserByUsername(profileDto.getUserName());
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
        BaseUser baseUser = baseUserService.addUser(profileDto);
        if (baseUser == null) {
            return null;
        }
        //默认注册用户名账户
        BaseUserAccount baseUserAccount = this.registerUsernameAccount(baseUser.getUserId(), profileDto.getUserName(), encodePassword);
        if (baseUserAccount != null && StringUtils.isNotBlank(profileDto.getEmail())) {
            //注册email账号登陆
            this.registerEmailAccount(baseUser.getUserId(), profileDto.getEmail(), encodePassword);
        }
        if (baseUserAccount != null && StringUtils.isNotBlank(profileDto.getMobile())) {
            //注册手机号账号登陆
            this.registerMobileAccount(baseUser.getUserId(), profileDto.getMobile(), encodePassword);
        }
        return baseUser.getUserId();
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
    public BaseUserAccount register(String account, String password, String accountType, String nickName) {
        if (isExist(account, accountType)) {
            return null;
        }
        BaseUserDto user = new BaseUserDto();
        String name = accountType + "_" + RandomValueUtils.randomNumeric(16);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setUserName(name);
        user.setUserType(BaseConstants.USER_TYPE_PLATFORM);
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        user.setRegisterTime(user.getCreateTime());
        user.setStatus(BaseConstants.USER_STATE_NORMAL);
        BaseUser baseUser = baseUserService.addUser(user);
        if (baseUser == null) {
            return null;
        }
        //加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        BaseUserAccount baseUserAccount = new BaseUserAccount(baseUser.getUserId(), account, encodePassword, accountType);
        baseUserAccountMapper.insert(baseUserAccount);
        return baseUserAccount;
    }

    /**
     * 注册第三方账号
     *
     * @param username
     * @param userhead
     * @param accountType
     * @param nickName
     * @return
     */
    @Override
    public BaseUserAccount registerThirdAccount(String username, String avatar, String authType, String nickName) {
        BaseUserAccount baseUserAccount = getUserAccount(username, authType);
        if (ObjectUtils.isEmpty(baseUserAccount)) {
            //加密
            String encodePassword = passwordEncoder.encode(BaseConstants.DEF_PWD);
            baseUserAccount = new BaseUserAccount(username, encodePassword, authType, nickName, avatar);
            baseUserAccountMapper.insert(baseUserAccount);
        }
        return baseUserAccount;
    }
    public BaseUserAccount getUserAccount(String account, String accountType) {
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getAccount, account)
                .eq(BaseUserAccount::getAccountType, accountType);
        return baseUserAccountMapper.selectOne(queryWrapper);

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
        BaseUserAccount baseUserAccount = null;
        // 账号返回数据
        BaseUserAccountDto baseUserAccountDto = null;
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(thirdParty)) {
            queryWrapper.lambda()
                    .eq(BaseUserAccount::getAccountType, thirdParty)
                    .eq(BaseUserAccount::getAccount, account);
            baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);
        } else {
            // 非第三方登录, 账号密码方式登陆
            //用户名登录
            queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_USERNAME)
                    .eq(BaseUserAccount::getAccount, account);
            baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);

            // 手机号登陆
            if (baseUserAccount == null && StringUtils.matchMobile(account)) {
                // 用户名登陆
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda()
                        .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_MOBILE)
                        .eq(BaseUserAccount::getAccount, account);
                baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);
            }

            // 邮箱登陆
            if (baseUserAccount == null && StringUtils.matchEmail(account)) {
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda()
                        .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_EMAIL)
                        .eq(BaseUserAccount::getAccount, account);
                baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);
            }
        }

        // 获取用户详细信息
        if (baseUserAccount != null) {
            baseUserAccountDto = new BaseUserAccountDto();
            BeanUtils.copyProperties(baseUserAccount, baseUserAccountDto);
            baseUserAccountDto.setUserProfile(baseUserService.getUserWithAuthoritiesById(baseUserAccount.getUserId()));
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    BaseUserAccountLogs log = new BaseUserAccountLogs();
                    log.setUserId(baseUserAccount.getUserId());
                    log.setAccount(baseUserAccount.getAccount());
                    log.setAccountId(String.valueOf(baseUserAccount.getAccountId()));
                    log.setAccountType(baseUserAccount.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    addLoginLog(log);
                }
            } catch (Exception e) {
                log.error("添加登录日志失败:{}", e);
            }
        }
        return baseUserAccountDto;
    }

    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param accountName
     * @return
     */
    @Override
    public BaseUserAccountDto applogin(String accountName) {
        if (StringUtils.isBlank(accountName)) {
            return null;
        }
        Map<String, String> headers = WebUtils.getHttpHeaders(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String thirdParty = headers.get(AuthConstants.HEADER_X_THIRDPARTY_LOGIN);
        BaseUserAccount account = null;
        // 账号返回数据
        BaseUserAccountDto userAccountDto = null;
        QueryWrapper<BaseUserAccount> qw = new QueryWrapper();
        qw.lambda().eq(BaseUserAccount::getAccount, accountName);
        if (StringUtils.isNotBlank(thirdParty)) {
            //第三方登录加入登录类型,防止不同平台的三方账户重复
            qw.lambda().eq(BaseUserAccount::getAccountType, thirdParty);
        }
        account = baseUserAccountMapper.selectOne(qw);
        // 获取用户详细信息
        if (account != null) {
            userAccountDto = new BaseUserAccountDto();
            BeanUtils.copyProperties(account, userAccountDto);
        }
        return userAccountDto;
    }

    /**
     * 注册系统用户名账户
     *
     * @param userId
     * @param username
     * @param password
     */
    @Override
    public BaseUserAccount registerUsernameAccount(Long userId, String username, String password) {
        if (isExist(userId, username, BaseConstants.USER_ACCOUNT_TYPE_USERNAME)) {
            //已经注册
            return null;
        }
        BaseUserAccount baseUserAccount = new BaseUserAccount(userId, username, password, BaseConstants.USER_ACCOUNT_TYPE_USERNAME);
        baseUserAccountMapper.insert(baseUserAccount);
        return baseUserAccount;
    }

    /**
     * 注册email账号
     *
     * @param userId
     * @param email
     * @param password
     */
    @Override
    public BaseUserAccount registerEmailAccount(Long userId, String email, String password) {
        if (!StringUtils.matchEmail(email)) {
            return null;
        }
        if (isExist(userId, email, BaseConstants.USER_ACCOUNT_TYPE_EMAIL)) {
            //已经注册
            return null;
        }
        BaseUserAccount baseUserAccount = new BaseUserAccount(userId, email, password, BaseConstants.USER_ACCOUNT_TYPE_EMAIL);
        baseUserAccountMapper.insert(baseUserAccount);
        return baseUserAccount;
    }


    /**
     * 注册手机账号
     *
     * @param userId
     * @param mobile
     * @param password
     */
    @Override
    public BaseUserAccount registerMobileAccount(Long userId, String mobile, String password) {
        if (!StringUtils.matchMobile(mobile)) {
            return null;
        }
        if (isExist(userId, mobile, BaseConstants.USER_ACCOUNT_TYPE_MOBILE)) {
            //已经注册
            return null;
        }
        BaseUserAccount baseUserAccount = new BaseUserAccount(userId, mobile, password, BaseConstants.USER_ACCOUNT_TYPE_MOBILE);
        baseUserAccountMapper.insert(baseUserAccount);
        return baseUserAccount;
    }


    /**
     * 重置用户密码
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
        BaseUser userProfile = baseUserService.getUserById(userId);
        if (userProfile == null) {
            throw new OpenAlertException("用户信息不存在!");
        }
        if (CommonConstants.ROOT.equals(userProfile.getUserName())) {
            throw new OpenAlertException("默认用户不允许修改!");
        }
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getUserId, userId)
                .eq(BaseUserAccount::getAccount, userProfile.getUserName())
                .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_USERNAME);
        BaseUserAccount baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);
        if (baseUserAccount == null) {
            return;
        }
        String oldPasswordEncoder = passwordEncoder.encode(oldPassword);
        if (!passwordEncoder.matches(baseUserAccount.getPassword(), oldPasswordEncoder)) {
            throw new OpenAlertException("原密码错误!");
        }
        baseUserAccount.setPassword(passwordEncoder.encode(newPassword));
        baseUserAccountMapper.updateById(baseUserAccount);
    }


    /**
     * 重置用户密码
     *
     * @param userId
     * @param password
     */
    @Override
    public void resetPassword(Long userId, String password) {
        if (userId == null || StringUtils.isBlank(password)) {
            return;
        }
        BaseUser userProfile = baseUserService.getUserById(userId);
        if (userProfile == null) {
            throw new OpenAlertException("用户信息不存在!");
        }
        if (CommonConstants.ROOT.equals(userProfile.getUserName())) {
            throw new OpenAlertException("默认用户不允许修改!");
        }
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getUserId, userId)
                .eq(BaseUserAccount::getAccount, userProfile.getUserName())
                .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_USERNAME);
        BaseUserAccount baseUserAccount = baseUserAccountMapper.selectOne(queryWrapper);
        if (baseUserAccount == null) {
            return;
        }
        baseUserAccount.setPassword(passwordEncoder.encode(password));
        baseUserAccountMapper.updateById(baseUserAccount);
    }


    /**
     * 更新系统用户登录Ip
     *
     * @param log
     */
    @Override
    public void addLoginLog(BaseUserAccountLogs log) {
        QueryWrapper<BaseUserAccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccountLogs::getUserId, log.getUserId());
        int count = systemUserAccountLogsMapper.selectCount(queryWrapper);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        systemUserAccountLogsMapper.insert(log);
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
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getUserId, userId)
                .eq(BaseUserAccount::getAccount, account)
                .eq(BaseUserAccount::getAccountType, accountType);
        int count = baseUserAccountMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    @Override
    public Boolean isExist(String account, String accountType) {
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getAccount, account)
                .eq(BaseUserAccount::getAccountType, accountType);
        int count = baseUserAccountMapper.selectCount(queryWrapper);
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
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getUserId, userId)
                .eq(BaseUserAccount::getAccount, email)
                .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_EMAIL);
        baseUserAccountMapper.delete(queryWrapper);
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
        QueryWrapper<BaseUserAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUserAccount::getUserId, userId)
                .eq(BaseUserAccount::getAccount, mobile)
                .eq(BaseUserAccount::getAccountType, BaseConstants.USER_ACCOUNT_TYPE_MOBILE);
        baseUserAccountMapper.delete(queryWrapper);
    }

}
