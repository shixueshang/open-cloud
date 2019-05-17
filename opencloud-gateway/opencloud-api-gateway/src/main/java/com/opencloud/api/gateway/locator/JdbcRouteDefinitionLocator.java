package com.opencloud.api.gateway.locator;

import com.google.common.collect.Lists;
import com.opencloud.api.gateway.event.GatewayRemoteRefreshRouteEvent;
import com.opencloud.api.gateway.event.GatewayResourceRefreshEvent;
import com.opencloud.base.client.model.entity.GatewayRoute;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态路由加载器
 *
 * @author liuyadu
 */
@Slf4j
public class JdbcRouteDefinitionLocator implements RouteDefinitionLocator, ApplicationListener<GatewayRemoteRefreshRouteEvent> {
    private JdbcTemplate jdbcTemplate;
    private Flux<RouteDefinition> routeDefinitions;
    private Map<String, List> cache = new HashMap<>();
    public ApplicationEventPublisher publisher;

    public JdbcRouteDefinitionLocator(JdbcTemplate jdbcTemplate,ApplicationEventPublisher publisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
        routeDefinitions = CacheFlux.lookup(cache, "routeDefs", RouteDefinition.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
    }

    /**
     * Clears the cache of routeDefinitions.
     *
     * @return routeDefinitions flux
     */
    public Flux<RouteDefinition> refresh() {
        this.cache.clear();
        this.routeDefinitions = this.loadRoutes();
        return this.routeDefinitions;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return this.routeDefinitions;
    }

    @Override
    public void onApplicationEvent(GatewayRemoteRefreshRouteEvent event) {
        refresh();
        this.publisher.publishEvent(new GatewayResourceRefreshEvent(this));
    }


    /**
     * 动态加载路由
     * * 示例
     * id: opencloud-admin-provider
     * uri: lb://opencloud-admin-provider
     * predicates:
     * - Path=/admin/**
     * - Name=平台后台管理服务
     * filters:
     * #转发去掉前缀,总要否则swagger无法加载
     * - StripPrefix=1
     *
     * @return
     */
    private Flux<RouteDefinition> loadRoutes() {
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
                    route.setRouteName(rs.getString("route_name"));
                    return route;
                }
            });
            if (routeList != null) {
                routeList.forEach(gatewayRoute -> {

                    RouteDefinition definition = new RouteDefinition();
                    List<PredicateDefinition> predicates = Lists.newArrayList();
                    List<FilterDefinition> filters = Lists.newArrayList();
                    definition.setId(gatewayRoute.getRouteId().toString());

                    // 路由条件
                    // 路由地址
                    PredicateDefinition predicatePath = new PredicateDefinition();
                    Map<String, String> predicatePathParams = new HashMap<>(8);
                    predicatePath.setName("Path");
                    predicatePathParams.put("name", StringUtils.isBlank(gatewayRoute.getRouteName()) ? gatewayRoute.getRouteId().toString() : gatewayRoute.getRouteName());
                    predicatePathParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", gatewayRoute.getPath());
                    predicatePath.setArgs(predicatePathParams);
                    predicates.add(predicatePath);

                    // 服务地址
                    URI uri = UriComponentsBuilder.fromUriString(StringUtils.isNotBlank(gatewayRoute.getUrl()) ? gatewayRoute.getUrl() : "lb://" + gatewayRoute.getServiceId()).build().toUri();

                    // 路由过滤器
                    FilterDefinition filterDefinition = new FilterDefinition();
                    Map<String, String> filterParams = new HashMap<>(8);
                    filterDefinition.setName("StripPrefix");
                    // 路径去前缀
                    filterParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
                    // 令牌桶流速
                    //filterParams.put("redis-rate-limiter.replenishRate", gatewayRoute.getLimiterRate());
                    //令牌桶容量
                    //filterParams.put("redis-rate-limiter.burstCapacity", gatewayRoute.getLimiterCapacity());
                    // 限流策略(#{@BeanName})
                    //filterParams.put("key-resolver", "#{@remoteAddrKeyResolver}");
                    filterDefinition.setArgs(filterParams);
                    filters.add(filterDefinition);

                    definition.setPredicates(predicates);
                    definition.setFilters(filters);
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
