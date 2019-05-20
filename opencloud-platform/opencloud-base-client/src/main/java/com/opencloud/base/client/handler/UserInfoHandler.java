package com.opencloud.base.client.handler;

import com.opencloud.base.client.model.BaseAppUserDto;

public interface UserInfoHandler {
    /**
     * 用户信息扩展
     *
     * @param baseUser
     * @return
     */
    BaseAppUserDto info(BaseAppUserDto baseUser);

    /**
     * 登录初始扩展
     *
     * @param baseUser
     * @return
     */
    BaseAppUserDto loginInit(BaseAppUserDto baseUser);
}
