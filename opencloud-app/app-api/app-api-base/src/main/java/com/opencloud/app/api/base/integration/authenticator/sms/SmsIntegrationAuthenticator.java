package com.opencloud.app.api.base.integration.authenticator.sms;


import com.opencloud.app.api.base.integration.authenticator.AbstractIntegrationAuthenticator;
import com.opencloud.app.api.base.integration.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.opencloud.app.api.base.integration.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.app.api.base.service.feign.BaseUserAccountRemoteService;
import com.opencloud.base.client.model.BaseUserAccountDto;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.model.ResultBody;

import com.opencloud.common.utils.ApiAssert;
import com.opencloud.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 短信验证码集成认证
 **/
@Component
public class SmsIntegrationAuthenticator extends AbstractIntegrationAuthenticator implements ApplicationEventPublisherAware {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private BaseUserAccountRemoteService accountRemoteService;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "SMS";

    @Override
    public ResultBody<BaseUserAccountDto> authenticate(IntegrationParams integrationAuthentication) {
        //获取密码，实际值是验证码
        String password = integrationAuthentication.getAuthParameter("password");
        //获取账户名，实际值是手机号
        String accountName = integrationAuthentication.getAccountName();
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(integrationAuthentication, accountName));
        //通过手机号码查询用户
        ResultBody<BaseUserAccountDto> sysUserAuthentication = accountRemoteService.appLogin(accountName);
        if (sysUserAuthentication != null) {
            //将密码设置为验证码
            //sysUserAuthentication.setPassword(passwordEncoder.encode(password));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(integrationAuthentication));
        }
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationParams integrationAuthentication) {
        String smsCode = integrationAuthentication.getAuthParameter("password");
        String accountName = integrationAuthentication.getAuthParameter("username");
        String v = redisUtils.getString(CommonConstants.PRE_SMS + accountName);
        ApiAssert.isNotEmpty("验证码无效", v);
        ApiAssert.eq("验证码不正确", smsCode, v);
    }

    @Override
    public boolean support(IntegrationParams integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
