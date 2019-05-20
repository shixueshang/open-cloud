package com.opencloud.app.api.base.integration.authenticator.password;


import com.opencloud.app.api.base.integration.model.IntegrationParams;
import org.springframework.stereotype.Component;

/**
 * 验证码认证
 **/
@Component
public class VerificationCodeIntegrationAuthenticator extends UsernamePasswordAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "vc";


    @Override
    public void prepare(IntegrationParams integrationAuthentication) {
        String vcToken = integrationAuthentication.getAuthParameter("vc_token");
        String vcCode = integrationAuthentication.getAuthParameter("vc_code");
       /* //验证验证码
        Result<Boolean> result = verificationCodeClient.validate(vcToken, vcCode, null);
        if (!result.getData()) {
            throw new OAuth2Exception("验证码错误");
        }*/
    }

    @Override
    public boolean support(IntegrationParams integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
