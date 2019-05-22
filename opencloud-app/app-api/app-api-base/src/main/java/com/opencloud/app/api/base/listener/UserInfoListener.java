package com.opencloud.app.api.base.listener;

import com.alibaba.fastjson.JSONObject;
import com.opencloud.base.client.event.UserInfoEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author liuyadu
 */
@Component
public class UserInfoListener implements ApplicationListener<UserInfoEvent> {
    /**
     * 注册监听实现方法
     *
     * @param userInfoEvent 用户注册事件
     */


    @Override
    public void onApplicationEvent(UserInfoEvent event) {
        //输出注册用户信息
        System.out.printf("UserRemoteApplicationEvent - Source : %s , originService : %s, destinationService : %s\n",
                JSONObject.toJSON(event.getSource()), event.getOriginService(), event.getDestinationService());
    }
}
