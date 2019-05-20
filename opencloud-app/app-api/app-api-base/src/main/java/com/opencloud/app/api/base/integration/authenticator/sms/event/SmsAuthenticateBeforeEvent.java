package com.opencloud.app.api.base.integration.authenticator.sms.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 短信认证之前的事件，可以监听事件进行用户手机号自动注册
 **/
@Getter
public class SmsAuthenticateBeforeEvent extends ApplicationEvent {
    //注册用户对象
    private String  userName;

    public SmsAuthenticateBeforeEvent(Object source, String userName) {
        super(source);
        this.userName = userName;
    }
}
