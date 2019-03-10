package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.entity.GatewayRoute;
import com.github.lyd.common.model.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 权限控制API接口
 * @author liuyadu
 */
public interface GatewayRouteRemoteApi {
    /**
     * 获取可用路由列表
     *
     * @return
     */
    @GetMapping("/gateway/route/list")
    ResultBody<List<GatewayRoute>> getRouteList();
}
