package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.event.UserInfoEvent;
import com.opencloud.base.client.model.BaseAppUserDto;
import com.opencloud.base.client.model.BaseUserDto;
import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.provider.mapper.BaseUserMapper;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseRoleService;
import com.opencloud.base.provider.service.BaseUserService;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.constants.MqConstants;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.Authority;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl extends BaseServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseRoleService roleService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private BusProperties busProperties;

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public BaseUser addUser(BaseUser baseUser) {
        baseUserMapper.insert(baseUser);
        return baseUser;
    }

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public BaseUser updateUser(BaseUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return null;
        }
        baseUserMapper.updateById(baseUser);
        return baseUser;
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseUser> findListPage(PageParams pageParams) {
        BaseUser query = pageParams.mapToObject(BaseUser.class);
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseUser::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), BaseUser::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), BaseUser::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return baseUserMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseUser> findAllList() {
        List<BaseUser> list = baseUserMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUser getUserById(Long userId) {
        return baseUserMapper.selectById(userId);
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUserDto getUserWithAuthoritiesById(Long userId) {
        // 用户权限列表
        List<Authority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map> roles = Lists.newArrayList();
        List<BaseRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                Map roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                Authority authority = new Authority(BaseConstants.ROLE_PREFIX + role.getRoleCode());
                authority.setOwner("role");
                authorities.add(authority);
            }
        }

        //查询系统用户资料
        BaseUser baseUser = getUserById(userId);

        // 加入用户权限
        List<Authority> userGrantedAuthority = baseAuthorityService.findUserGrantedAuthority(userId, CommonConstants.ROOT.equals(baseUser.getUserName()));
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        BaseUserDto userProfile = new BaseUserDto();
        BeanUtils.copyProperties(baseUser, userProfile);

        //设置用户资料,权限信息
        userProfile.setAuthorities(authorities);
        userProfile.setRoles(roles);
        return userProfile;
    }

    /**
     * 获取App用户详细信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseAppUserDto getAppUserWithByUserId(Long userId) {
        //查询系统用户资料
        BaseUser baseUser = getUserById(userId);
        BaseAppUserDto userProfile = new BaseAppUserDto();
        BeanUtils.copyProperties(baseUser, userProfile);
        //发布用户信息扩展事件
        userProfile = (BaseAppUserDto) amqpTemplate.convertSendAndReceive(MqConstants.QUEUE_USERINFO, userProfile);
        return userProfile;
    }

    /**
     * 登录初始化
     *
     * @param userId
     * @return
     */
    @Override
    public BaseAppUserDto loginInit() {
      /*  OpenUser user = OpenHelper.getUser();
        Long userId = Optional.ofNullable(user.getUserId()).orElse(-1L);
        BaseAppUserDto appUserDto = new BaseAppUserDto();
        BaseUser baseUser = new BaseUser();
        if (!CommonConstants.NULLKEY.equals(userId)) {
            //查询系统用户资料
            baseUser = getUserById(userId);
        } else {
            appUserDto.setAccountId(user.getAccountId());
            //首次登录
            appUserDto.setUserId(CommonConstants.NULLKEY);
        }
        BeanUtils.copyProperties(baseUser, appUserDto);
        */
        publisher.publishEvent(new UserInfoEvent(new BaseUser(), busProperties.getId(), null));
        //推送扩展事件
        //appUserDto = (BaseAppUserDto) amqpTemplate.convertSendAndReceive(MqConstants.QUEUE_LOGININIT, appUserDto);
        return null;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getUserByUsername(String username) {
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUser::getUserName, username);
        BaseUser saved = baseUserMapper.selectOne(queryWrapper);
        return saved;
    }


}
