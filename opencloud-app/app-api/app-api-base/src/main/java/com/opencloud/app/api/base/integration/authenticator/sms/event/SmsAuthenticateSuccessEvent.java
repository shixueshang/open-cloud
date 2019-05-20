package com.opencloud.app.api.base.integration.authenticator.sms.event;

import org.springframework.context.ApplicationEvent;

/**
 * 短信认证成功事件
 **/
public class SmsAuthenticateSuccessEvent extends ApplicationEvent {
    public SmsAuthenticateSuccessEvent(Object source) {
        super(source);
    }
}
