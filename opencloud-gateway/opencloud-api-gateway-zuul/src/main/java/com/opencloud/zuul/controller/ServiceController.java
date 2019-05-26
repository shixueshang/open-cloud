package com.opencloud.zuul.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.common.model.ResultBody;
import com.opencloud.zuul.locator.JdbcRouteLocator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ServiceController {

    @Autowired
    private JdbcRouteLocator zuulRoutesLocator;

    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping("/service/list")
    public ResultBody getServiceList() {
        List<Map> services = Lists.newArrayList();
        List<Route> routes = zuulRoutesLocator.getRoutes();
        if (routes != null && routes.size() > 0) {
            routes.forEach(route -> {
                Map service = Maps.newHashMap();
                service.put("serviceId", route.getId());
                service.put("serviceName", route.getId());
                services.add(service);
            });
        }
        return ResultBody.ok().data(services);
    }
}
