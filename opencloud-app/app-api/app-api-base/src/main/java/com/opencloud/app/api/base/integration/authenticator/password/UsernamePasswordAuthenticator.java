package com.opencloud.app.api.base.integration.authenticator.password;


import com.opencloud.app.api.base.integration.authenticator.AbstractIntegrationAuthenticator;
import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.app.api.base.service.feign.BaseUserAccountRemoteService;
import com.opencloud.base.client.model.BaseUserAccountDto;
import com.opencloud.common.model.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 默认登录处理(账户名密码登录)
 **/
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractIntegrationAuthenticator {
    private final static String DEFAULT_AUTH_TYPE = "PWD";

    @Autowired
    private BaseUserAccountRemoteService accountRemoteService;

    @Override
    public ResultBody<BaseUserAccountDto> authenticate(IntegrationParams integrationAuthentication) {
        return accountRemoteService.appLogin(integrationAuthentication.getAccountName());
    }

    @Override
    public void prepare(IntegrationParams integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationParams integrationAuthentication) {
        return DEFAULT_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
