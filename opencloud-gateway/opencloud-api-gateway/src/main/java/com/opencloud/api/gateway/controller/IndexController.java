package com.opencloud.api.gateway.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.api.gateway.configuration.ApiProperties;
import com.opencloud.common.model.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
    private RouteLocator routeLocator;
    @Autowired
    private GatewayProperties gatewayProperties;

    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/")
    public String index() {
        if (apiProperties.getEnableSwaggerUi()) {
            return "redirect:swagger-ui.html";
        }
        return "index";
    }

    @GetMapping("/service/list")
    @ResponseBody
    public Mono<ResultBody> getServiceList() {
        List<Map> services = Lists.newArrayList();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .forEach(predicateDefinition -> {
                                    Map service = Maps.newHashMap();
                                    service.put("serviceId", routeDefinition.getId());
                                    service.put("serviceName",  predicateDefinition.getArgs().get("name"));
                                    services.add(service);
                                }
                        ));
        return Mono.just(ResultBody.success(services));
    }
}
