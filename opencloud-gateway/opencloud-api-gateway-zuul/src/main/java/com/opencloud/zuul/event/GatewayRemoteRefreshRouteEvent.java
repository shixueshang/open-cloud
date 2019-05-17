package com.opencloud.zuul.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关刷新远程事件
 * @author liuyadu
 */
public class GatewayRemoteRefreshRouteEvent extends RemoteApplicationEvent {

    private GatewayRemoteRefreshRouteEvent() {
    }

    public GatewayRemoteRefreshRouteEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }
}
