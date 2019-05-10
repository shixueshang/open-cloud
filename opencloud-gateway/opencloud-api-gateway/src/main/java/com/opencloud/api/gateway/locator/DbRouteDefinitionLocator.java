package com.opencloud.api.gateway.locator;

import com.google.common.collect.Lists;
import com.opencloud.base.client.model.entity.GatewayRoute;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Slf4j
public class DbRouteDefinitionLocator implements RouteDefinitionLocator {
    private JdbcTemplate jdbcTemplate;

    public DbRouteDefinitionLocator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        //从数据库拿到路由配置
        List<RouteDefinition> routes = Lists.newArrayList();
        try {
            List<GatewayRoute> routeList = jdbcTemplate.query("SELECT * FROM gateway_route WHERE status = 1", new RowMapper<GatewayRoute>() {
                @Override
                public GatewayRoute mapRow(ResultSet rs, int i) throws SQLException {
                    GatewayRoute route = new GatewayRoute();
                    route.setRouteId(rs.getLong("route_id"));
                    route.setPath(rs.getString("path"));
                    route.setServiceId(rs.getString("service_id"));
                    route.setUrl(rs.getString("url"));
                    route.setStatus(rs.getInt("status"));
                    route.setRetryable(rs.getInt("retryable"));
                    route.setStripPrefix(rs.getInt("strip_prefix"));
                    route.setIsPersist(rs.getInt("is_persist"));
                    route.setRouteDesc(rs.getString("route_desc"));
                    return route;
                }
            });
            if (routeList != null) {
                routeList.forEach(gatewayRoute -> {
                    RouteDefinition definition = new RouteDefinition();
                    Map<String, String> predicateParams = new HashMap<>(8);
                    PredicateDefinition predicate = new PredicateDefinition();
                    FilterDefinition filterDefinition = new FilterDefinition();
                    Map<String, String> filterParams = new HashMap<>(8);
                    definition.setId(gatewayRoute.getRouteId().toString());
                    predicate.setName("Path");
                    predicateParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", gatewayRoute.getPath());
                    URI uri = UriComponentsBuilder.fromUriString(StringUtils.isNotBlank(gatewayRoute.getUrl()) ? gatewayRoute.getUrl() : "lb://" + gatewayRoute.getServiceId()).build().toUri();
                    filterDefinition.setName("StripPrefix");
                    // 路径去前缀
                    filterParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", gatewayRoute.getStripPrefix().toString());
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
                    routes.add(definition);
                });
            }
            log.info("=============加载动态路由:{}==============", routes.size());
        } catch (Exception e) {
            log.error("加载动态路由错误:{}", e.getMessage());
        }
        return Flux.fromIterable(routes);
    }
}
