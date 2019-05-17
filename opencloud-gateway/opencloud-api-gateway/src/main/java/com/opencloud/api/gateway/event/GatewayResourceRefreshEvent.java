package com.opencloud.api.gateway.event;

import org.springframework.context.ApplicationEvent;

/**
 * 自定义网关刷新远程事件
 * @author liuyadu
 */
public class GatewayResourceRefreshEvent extends ApplicationEvent {


    public GatewayResourceRefreshEvent(Object source) {
        super(source);
    }
}
