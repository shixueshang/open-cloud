package com.opencloud.app.api.base.listener;

import com.opencloud.base.client.event.UserInfoEvent;
import com.opencloud.base.client.model.entity.BaseUser;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserInfoListener implements ApplicationListener<UserInfoEvent> {
    /**
     * 注册监听实现方法
     *
     * @param userInfoEvent 用户注册事件
     */


    @Override
    public void onApplicationEvent(UserInfoEvent userInfoEvent) {
        //获取注册用户对象
        BaseUser user = userInfoEvent.getUser();
        //输出注册用户信息
        System.out.println("@EventListener注册信息，用户名：" + user.getUserName() + "，密码：" + user.getMobile());

    }
}
