package com.opencloud.app.api.base.integration.authenticator;


import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.base.client.model.BaseUserAccountDto;
import com.opencloud.common.model.ResultBody;

/**
 * 统一登录父类
 **/
public abstract class AbstractIntegrationAuthenticator implements IntegrationAuthenticator {

    @Override
    public abstract ResultBody<BaseUserAccountDto> authenticate(IntegrationParams integrationParams);

    @Override
    public abstract void prepare(IntegrationParams integrationParams);

    @Override
    public abstract boolean support(IntegrationParams integrationParams);

    @Override
    public void complete(IntegrationParams integrationParams) {

    }
}
