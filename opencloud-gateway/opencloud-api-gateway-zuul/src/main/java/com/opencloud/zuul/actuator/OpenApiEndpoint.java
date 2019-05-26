package com.opencloud.zuul.actuator;

import com.opencloud.common.event.GatewayRemoteRefreshRouteEvent;
import com.opencloud.common.model.ResultBody;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.bus.endpoint.AbstractBusEndpoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
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
        this.publish(new GatewayRemoteRefreshRouteEvent(this, this.getInstanceId(), destination));
        return ResultBody.ok();
    }
}
