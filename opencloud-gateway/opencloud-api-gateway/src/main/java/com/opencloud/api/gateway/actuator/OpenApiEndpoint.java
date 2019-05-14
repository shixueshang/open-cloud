package com.opencloud.api.gateway.actuator;

import com.opencloud.api.gateway.actuator.event.GatewayRefreshRemoteApplicationEvent;
import com.opencloud.common.model.ResultBody;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.bus.endpoint.AbstractBusEndpoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义网关刷新端点
 * @author liuyadu
 */
@RestControllerEndpoint(
        id = "open"
)
public class OpenApiEndpoint extends AbstractBusEndpoint {

    public OpenApiEndpoint(ApplicationEventPublisher context, String id) {
        super(context, id);
    }

    /**
     * 支持灰度发布
     * /actuator/open/refresh?destination = customers：**
     *
     * @param destination
     */
    @PostMapping("/refresh")
    public ResultBody busRefreshWithDestination(@RequestParam(required = false)  String destination) {
        this.publish(new GatewayRefreshRemoteApplicationEvent(this, this.getInstanceId(), destination));
        return ResultBody.success();
    }
}
