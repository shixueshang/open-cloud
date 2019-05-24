package com.opencloud.app.api.base.integration.authenticator.third;



import com.opencloud.app.api.base.integration.authenticator.AbstractIntegrationAuthenticator;
import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.app.api.base.service.feign.BaseUserAccountRemoteService;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.common.model.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * 第三方登录集成认证
 **/
@Component
public class ThirdIntegrationAuthenticator extends AbstractIntegrationAuthenticator implements ApplicationEventPublisherAware {


    @Autowired
    private BaseUserAccountRemoteService accountRemoteService;

    private ApplicationEventPublisher applicationEventPublisher;

    public final static String SMS_AUTH_TYPE = "QQ,WEIXIN,WEIBO";

    @Override
    public ResultBody<UserAccount> authenticate(IntegrationParams integrationParams) {
        //唯一标识openId
        String username = integrationParams.getAuthParameter("username");
        //头像
        String avatar = integrationParams.getAuthParameter("avatar");
        //昵称
        String nickName = integrationParams.getAuthParameter("nickName");
        //第三方登录类型,微信,qq,微博
        String authType = integrationParams.getAuthType();
        return accountRemoteService.registerThirdPartyAccount(username, avatar, authType, nickName);
    }

    @Override
    public void prepare(IntegrationParams integrationParams) {

    }

    @Override
    public boolean support(IntegrationParams integrationAuthentication) {
        return SMS_AUTH_TYPE.contains(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
