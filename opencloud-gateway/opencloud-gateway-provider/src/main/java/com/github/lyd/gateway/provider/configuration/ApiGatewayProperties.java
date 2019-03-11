package com.github.lyd.gateway.provider.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义网关配置
 *
 * @author: liuyadu
 * @date: 2018/11/23 14:40
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.api-gateway")
public class ApiGatewayProperties {
    /**
     * 是否开启签名验证
     */
    private Boolean checkSign = true;
    /**
     * 是否开启动态访问控制
     */
    private Boolean accessControl = true;

    public Boolean getCheckSign() {
        return checkSign;
    }

    public void setCheckSign(Boolean checkSign) {
        this.checkSign = checkSign;
    }

    public Boolean getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(Boolean accessControl) {
        this.accessControl = accessControl;
    }
}
