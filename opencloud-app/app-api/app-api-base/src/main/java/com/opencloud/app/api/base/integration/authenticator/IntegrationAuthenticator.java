package com.opencloud.app.api.base.integration.authenticator;

import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.base.client.model.BaseUserAccountDto;
import com.opencloud.common.model.ResultBody;

/**
 * 统一登录处理接口
 **/
public interface IntegrationAuthenticator {

    /**
     * 处理集成认证
     *
     * @param integrationParams
     * @return
     */
    ResultBody<BaseUserAccountDto> authenticate(IntegrationParams integrationParams);


    /**
     * 进行预处理
     *
     * @param integrationParams
     */
    void prepare(IntegrationParams integrationParams);

    /**
     * 判断是否支持集成认证类型
     *
     * @param integrationParams
     * @return
     */
    boolean support(IntegrationParams integrationParams);

    /**
     * 认证结束后执行
     *
     * @param integrationParams
     */
    void complete(IntegrationParams integrationParams);

}
