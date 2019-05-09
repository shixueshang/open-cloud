package com.opencloud.zuul.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.common.model.ResultBody;
import com.opencloud.zuul.configuration.ApiProperties;
import com.opencloud.zuul.locator.DbRouteLocator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/5 16:33
 * @description:
 */
@Controller
public class IndexController {
    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private DbRouteLocator zuulRoutesLocator;

    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/")
    public String index() {
        if (apiProperties.getEnableSwaggerUi()) {
            return "redirect:swagger-ui.html";
        }
        return "index";
    }

    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping("/service/list")
    @ResponseBody
    public ResultBody getServiceList() {
        List<Map> services = Lists.newArrayList();
        Map gateway = Maps.newHashMap();
        gateway.put("serviceId", serviceId);
        gateway.put("serviceName", serviceId);
        services.add(gateway);

        List<Route> routes = zuulRoutesLocator.getRoutes();
        if (routes != null && routes.size() > 0) {
            routes.forEach(route -> {
                Map service = Maps.newHashMap();
                service.put("serviceId", route.getId());
                service.put("serviceName", route.getId());
                services.add(service);
            });
        }

        return ResultBody.success(services);
    }
}
