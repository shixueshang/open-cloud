package com.opencloud.app.api.server.handler;

import com.opencloud.base.client.handler.UserInfoHandler;
import com.opencloud.base.client.model.BaseAppUserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * APP接口扩展
 */
@Component
@Log4j2
public class GetUserInfoHandler implements UserInfoHandler {

    @Override
    public BaseAppUserDto info(BaseAppUserDto baseAppUserDto) {
        log.debug("用户信息扩展" + baseAppUserDto.getUserName());
        return baseAppUserDto;
    }

    @Override
    public BaseAppUserDto loginInit(BaseAppUserDto baseAppUserDto) {
        log.debug("登录初始化扩展" + baseAppUserDto.getUserName());
        return baseAppUserDto;
    }
}
