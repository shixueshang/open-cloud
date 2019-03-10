package com.github.lyd.auth.client.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/2 18:02
 * @description:
 */
public class BaseClientDetailsDto extends BaseClientDetails implements Serializable {
    private static final long serialVersionUID = 3725084953460581042L;

    public BaseClientDetailsDto() {
        super();
    }

    public BaseClientDetailsDto(ClientDetails prototype) {
       super(prototype);
    }
    /**
     * @param clientId       应用ID
     * @param clientSecret   应用秘钥
     * @param grantTypes     授权类型
     * @param autoApproveScopes    自动授权
     * @param redirectUrls   授权重定向地址
     * @param scopes         授权范围
     * @param resourceIds    资源服务ID
     * @param authorities    权限
     * @param additionalInfo 客户端附加信息,json字符串
     */
    public BaseClientDetailsDto(String clientId, String clientSecret, String grantTypes, String autoApproveScopes, String redirectUrls, String scopes, String resourceIds, String authorities, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInfo) {
        super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUrls);
        try {
            this.setClientSecret(clientSecret);
            Map appInfo = JSONObject.parseObject(additionalInfo, Map.class);
            this.setAdditionalInformation(appInfo);
            if (StringUtils.hasText(autoApproveScopes)) {
                this.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(autoApproveScopes));
            }
            this.setAccessTokenValiditySeconds(accessTokenValidity);
            this.setRefreshTokenValiditySeconds(refreshTokenValidity);
        } catch (Exception e) {

        }
    }
}
