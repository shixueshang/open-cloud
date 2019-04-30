package com.opencloud.base.provider.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.BaseUserDto;
import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.provider.mapper.BaseUserMapper;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseRoleService;
import com.opencloud.base.provider.service.BaseUserService;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.mapper.ExampleBuilder;
import com.opencloud.common.model.PageList;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.security.OpenGrantedAuthority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseRoleService roleService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public BaseUser addUser(BaseUser baseUser) {
        baseUserMapper.insertSelective(baseUser);
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
        baseUserMapper.updateByPrimaryKeySelective(baseUser);
        return baseUser;
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseUser> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseUser query = pageParams.mapToObject(BaseUser.class);
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .andEqualTo("userId", query.getUserId())
                .andEqualTo("userType", query.getUserType())
                .andEqualTo("userName", query.getUserName())
                .andEqualTo("mobile", query.getMobile()).end().build();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseUser> findList() {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria().end().build();
        example.orderBy("userId").asc();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
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
        return baseUserMapper.selectByPrimaryKey(userId);
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
        List<OpenGrantedAuthority> authorities = Lists.newArrayList();
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
                OpenGrantedAuthority authority = new OpenGrantedAuthority(BaseConstants.ROLE_PREFIX + role.getRoleCode());
                authority.setOwner("role");
                authorities.add(authority);
            }
        }

        //查询系统用户资料
        BaseUser baseUser = getUserById(userId);

        // 加入用户权限
        List<OpenGrantedAuthority> userGrantedAuthority = baseAuthorityService.findUserGrantedAuthority(userId, CommonConstants.ROOT.equals(baseUser.getUserName()));
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
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getUserByUsername(String username) {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .andEqualTo("userName", username)
                .end().build();
        BaseUser saved = baseUserMapper.selectOneByExample(example);
        return saved;
    }


}
