package com.opencloud.app.api.base.listener;

import com.opencloud.base.client.handler.UserInfoHandler;
import com.opencloud.base.client.model.BaseAppUserDto;
import com.opencloud.common.constants.MqConstants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = MqConstants.QUEUE_USERINFO)
public class Receiver {
    @Resource
    private AutowireCapableBeanFactory spring;
    @Autowired
    private UserInfoHandler userInfoHandler;

    @RabbitHandler
    public BaseAppUserDto process(BaseAppUserDto object) {
        System.out.println("Receiver:" + object);
        object.setAccount("test");
        userInfoHandler.info(object);
        return object;
    }
}
