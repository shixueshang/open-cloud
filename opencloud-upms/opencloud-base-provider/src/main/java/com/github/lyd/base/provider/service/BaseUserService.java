package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.BaseUserDto;
import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface BaseUserService {

    /**
     * 更新系统用户
     *
     * @param profileDto
     * @return
     */
    Long addProfile(BaseUserDto profileDto);

    /**
     * 更新系统用户
     *
     * @param profileDto
     * @return
     */
    Boolean updateProfile(BaseUserDto profileDto);

    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    PageList<BaseUser> findListPage(PageParams pageParams, String keyword);

    /**
     * 查询列表
     *
     * @param keyword
     * @return
     */
    List<BaseUser> findList(String keyword);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseUser getProfile(String username);

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    BaseUser getProfile(Long userId);
}
