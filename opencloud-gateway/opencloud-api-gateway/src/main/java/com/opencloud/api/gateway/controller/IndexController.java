package com.opencloud.api.gateway.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.api.gateway.configuration.ApiProperties;
import com.opencloud.common.model.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

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
    private RouteDefinitionLocator routeDefinitionLocator;

    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/")
    public String index() {
        if (apiProperties.getApiDebug()) {
            return "redirect:doc.html";
        }
        return "index";
    }

    @GetMapping("/service/list")
    @ResponseBody
    public Mono<ResultBody> getServiceList() {
        List<Map> services = Lists.newArrayList();
        routeDefinitionLocator.getRouteDefinitions()
                .subscribe(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            Map service = Maps.newHashMap();
                            service.put("serviceId", routeDefinition.getId());
                            service.put("serviceName", predicateDefinition.getArgs().get("name"));
                            services.add(service);
                        }));
        return Mono.just(ResultBody.ok().data(services));
    }
}
