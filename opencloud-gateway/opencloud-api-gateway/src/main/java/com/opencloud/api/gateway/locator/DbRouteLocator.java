package com.opencloud.api.gateway.locator;

import com.google.common.collect.Lists;
import com.opencloud.api.gateway.service.feign.GatewayRemoteService;
import com.opencloud.base.client.model.entity.GatewayRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.CachingRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2019/5/9 17:40
 * @description:
 */
public class DbRouteLocator extends CachingRouteDefinitionLocator {
    @Autowired
    private GatewayRemoteService gatewayRemoteService;

    public DbRouteLocator(RouteDefinitionLocator delegate, GatewayRemoteService gatewayRemoteService) {
        super(delegate);
    }

    public void locateRoutes() {
        //从数据库拿到路由配置
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        List<GatewayRoute> gatewayRouteList = gatewayRemoteService.getApiRouteList().getData();
        if(gatewayRouteList!=null){
            gatewayRouteList.forEach(gatewayRoute -> {
                RouteDefinition definition = new RouteDefinition();
                Map<String, String> predicateParams = new HashMap<>(8);
                PredicateDefinition predicate = new PredicateDefinition();
                FilterDefinition filterDefinition = new FilterDefinition();
                Map<String, String> filterParams = new HashMap<>(8);
                definition.setId(gatewayRoute.getRouteId().toString());
                predicate.setName("Path");
                predicateParams.put("pattern", gatewayRoute.getPath());
                URI uri = UriComponentsBuilder.fromUriString("lb://" + gatewayRoute.getServiceId()).build().toUri();
                filterDefinition.setName("StripPrefix");
                // 路径去前缀
                filterParams.put("_genkey_0", gatewayRoute.getStripPrefix().toString());
              /*  // 令牌桶流速
                filterParams.put("redis-rate-limiter.replenishRate", gatewayRoute.getLimiterRate());
                //令牌桶容量
                filterParams.put("redis-rate-limiter.burstCapacity", gatewayRoute.getLimiterCapacity());
                // 限流策略(#{@BeanName})
                filterParams.put("key-resolver", "#{@remoteAddrKeyResolver}");*/
                predicate.setArgs(predicateParams);
                filterDefinition.setArgs(filterParams);
                definition.setPredicates(Arrays.asList(predicate));
                definition.setFilters(Arrays.asList(filterDefinition));
                definition.setUri(uri);
                routeDefinitions.add(definition);
            });
            this.getRouteDefinitions().concatWithValues(routeDefinitions.toArray(new  RouteDefinition[routeDefinitions.size()]));
        }
    }

    @Override
    public Flux<RouteDefinition> refresh() {
        super.refresh();
        locateRoutes();
        return getRouteDefinitions();
    }

}
