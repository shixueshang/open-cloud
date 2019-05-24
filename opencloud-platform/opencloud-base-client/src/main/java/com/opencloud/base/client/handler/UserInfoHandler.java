package com.opencloud.base.client.handler;

import com.opencloud.base.client.model.AppUser;

public interface UserInfoHandler {
    /**
     * 用户信息扩展
     *
     * @param baseUser
     * @return
     */
    AppUser info(AppUser baseUser);

    /**
     * 登录初始扩展
     *
     * @param baseUser
     * @return
     */
    AppUser loginInit(AppUser baseUser);
}
