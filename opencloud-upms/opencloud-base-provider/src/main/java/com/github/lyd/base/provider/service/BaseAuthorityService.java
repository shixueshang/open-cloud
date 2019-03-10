package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.client.model.entity.BaseAuthority;
import com.github.lyd.common.security.OpenGrantedAuthority;

import java.util.Date;
import java.util.List;

/**
 * 系统权限管理
 *
 * @author liuyadu
 */
public interface BaseAuthorityService {

    /**
     * 获取所有可用权限
     *
     * @param type      = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @param serviceId
     * @return
     */
    List<BaseAuthorityDto> findAuthority(Integer type, String serviceId);

    /**
     * 保存或修改权限
     *
     * @param resourceId
     * @param resourceType
     * @return 权限Id
     */
    Long saveOrUpdateAuthority(Long resourceId, ResourceType resourceType);


    /**
     * 获取权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    BaseAuthority getAuthority(Long resourceId, ResourceType resourceType);

    /**
     * 移除权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    void removeAuthority(Long resourceId, ResourceType resourceType);


    /**
     * 是否已被授权
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    Boolean isGranted(Long resourceId, ResourceType resourceType);

    /**
     * 角色授权
     *
     * @param
     * @param roleId       角色ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addRoleAuthority(Long roleId, Date expireTime, String... authorityIds);


    /**
     * 用户授权
     *
     * @param
     * @param userId       用户ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addUserAuthority(Long userId, Date expireTime, String... authorityIds);


    /**
     * 应用授权
     *
     * @param
     * @param appId        应用ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    void addAppAuthority(String appId, Date expireTime, String... authorityIds);

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<OpenGrantedAuthority> findAppGrantedAuthority(String appId);

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<OpenGrantedAuthority> findRoleGrantedAuthority(Long roleId);

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<OpenGrantedAuthority> findUserGrantedAuthority(Long userId);


    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @param root
     * @return
     */
    List<BaseAuthorityDto> findUserGrantedAuthorityDetail(Long userId, Boolean root);


}
