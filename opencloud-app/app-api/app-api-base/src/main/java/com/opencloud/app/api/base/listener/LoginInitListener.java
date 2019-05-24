package com.opencloud.app.api.base.listener;

import com.opencloud.base.client.handler.UserInfoHandler;
import com.opencloud.base.client.model.AppUser;
import com.opencloud.common.constants.MqConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户登录初始化监听扩展
 */
@Component
@RabbitListener(queues = MqConstants.QUEUE_LOGININIT)
@Log4j2
public class LoginInitListener {
    @Autowired
    private UserInfoHandler userInfoHandler;

    @RabbitHandler
    public AppUser process(AppUser object) {
        log.debug("初始化登录:" + object.getUserName());
        userInfoHandler.loginInit(object);
        return object;
    }
}
