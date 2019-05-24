package com.opencloud.base.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.AppUser;
import com.opencloud.base.client.model.UserInfo;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface BaseUserService extends IBaseService<BaseUser> {

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    BaseUser addUser(BaseUser baseUser);

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    BaseUser updateUser(BaseUser baseUser);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseUser> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseUser> findAllList();


    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    BaseUser getUserById(Long userId);

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    UserInfo getUserWithAuthoritiesById(Long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseUser getUserByUsername(String username);


    /**
     * 登录初始化
     *
     * @param userId
     * @return
     */
    AppUser loginInit();


    /**
     * 获取App用户详细信息
     *
     * @param userId
     * @return
     */
    AppUser getAppUserWithByUserId(Long userId);


}
