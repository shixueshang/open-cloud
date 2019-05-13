package com.opencloud.api.gateway.locator;

import com.google.common.collect.Maps;
import com.opencloud.api.gateway.actuator.event.GatewayRefreshRemoteApplicationEvent;
import com.opencloud.api.gateway.service.feign.BaseAuthorityRemoteService;
import com.opencloud.api.gateway.service.feign.GatewayRemoteService;
import com.opencloud.base.client.model.AccessAuthority;
import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义动态权限加载器
 *
 * @author liuyadu
 */
@Slf4j
public class ApiAccessLocator implements ApplicationListener<GatewayRefreshRemoteApplicationEvent>{
    /**
     * 单位时间
     */
    /**
     * 1分钟
     */
    public static final long SECONDS_IN_MINUTE = 60;
    /**
     * 一小时
     */
    public static final long SECONDS_IN_HOUR = 3600;
    /**
     * 一天
     */
    public static final long SECONDS_IN_DAY = 24 * 3600;

    /**
     * 请求总时长
     */
    public static final int PERIOD_SECOND_TTL = 10;
    public static final int PERIOD_MINUTE_TTL = 2 * 60 + 10;
    public static final int PERIOD_HOUR_TTL = 2 * 3600 + 10;
    public static final int PERIOD_DAY_TTL = 2 * 3600 * 24 + 10;


    private Flux<AccessAuthority> accessAuthorities;

    private Flux<GatewayIpLimitApisDto> ipBlacks;

    private Flux<GatewayIpLimitApisDto> ipWhites;

    /**
     * 缓存
     */
    private Map<String, List> cache = new HashMap<>();
    /**
     * 权限列表
     */
    private HashMap<String, Collection<ConfigAttribute>> allConfigAttributeCache;


    private BaseAuthorityRemoteService baseAuthorityRemoteService;
    private GatewayRemoteService gatewayRemoteService;

    private RouteDefinitionLocator routeDefinitionLocator;

    public ApiAccessLocator() {
        allConfigAttributeCache = Maps.newHashMap();
        accessAuthorities = CacheFlux.lookup(cache, "accessAuthorities", AccessAuthority.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipBlacks = CacheFlux.lookup(cache, "ipBlacks", GatewayIpLimitApisDto.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipWhites = CacheFlux.lookup(cache, "ipWhites", GatewayIpLimitApisDto.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
    }


    public ApiAccessLocator(RouteDefinitionLocator routeDefinitionLocator, BaseAuthorityRemoteService baseAuthorityRemoteService, GatewayRemoteService gatewayRemoteService) {
        this();
        this.baseAuthorityRemoteService = baseAuthorityRemoteService;
        this.gatewayRemoteService = gatewayRemoteService;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * 清空缓存并刷新
     */
    public void refresh() {
        this.cache.clear();
        this.allConfigAttributeCache.clear();
        loadAuthority();
        loadIpBlackList();
        loadIpWhiteList();
    }

    @Override
    public void onApplicationEvent(GatewayRefreshRemoteApplicationEvent event) {
        refresh();
        // 刷新路由
    }

    /**
     * 获取路由后的地址
     *
     * @return
     */
    protected String getFullPath(String serviceId, final String path) {
        routeDefinitionLocator.getRouteDefinitions()
                .filter(route -> route.getUri().equals("lb://" + serviceId))
                .subscribe(route -> route.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> Mono.just(predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                .replace("/**", path))));
        return path;
    }

    /**
     * 加载授权列表
     */
    public void loadAuthority() {
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        try {
            // 查询所有接口
            List<AccessAuthority> authorityList = baseAuthorityRemoteService.getAccessAuthorityList().getData();
            if (authorityList != null) {
                for (AccessAuthority item : authorityList) {
                    String path = item.getPath();
                    if (path == null) {
                        continue;
                    }
                    String fullPath = getFullPath(item.getServiceId(), path);
                    item.setPath(fullPath);
                    array = allConfigAttributeCache.get(fullPath);
                    if (array == null) {
                        array = new ArrayList<>();
                    }
                    if (!array.contains(item.getAuthority())) {
                        cfg = new SecurityConfig(item.getAuthority());
                        array.add(cfg);
                    }
                    allConfigAttributeCache.put(fullPath, array);
                }
                log.info("=============加载动态权限:{}==============", authorityList.size());
                accessAuthorities = Flux.fromIterable(authorityList);
            }
        } catch (Exception e) {
            log.error("加载动态权限错误:{}", e.getMessage());
        }
    }

    /**
     * 加载IP黑名单
     */
    public void loadIpBlackList() {
        try {
            List<GatewayIpLimitApisDto> ipBlackList = gatewayRemoteService.getApiBlackList().getData();
            if (ipBlackList != null) {
                for (GatewayIpLimitApisDto item : ipBlackList) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP黑名单:{}==============", ipBlackList.size());
                ipBlacks = Flux.fromIterable(ipBlackList);
            }
        } catch (Exception e) {
            log.error("加载IP黑名单错误:{}", e.getMessage());
        }
    }

    /**
     * 加载IP白名单
     */
    public void loadIpWhiteList() {
        try {
            List<GatewayIpLimitApisDto> ipWhiteList = gatewayRemoteService.getApiWhiteList().getData();
            if (ipWhiteList != null) {
                for (GatewayIpLimitApisDto item : ipWhiteList) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP白名单:{}==============", ipWhiteList.size());
                ipWhites = Flux.fromIterable(ipWhiteList);
            }
        } catch (Exception e) {
            log.error("加载IP白名单错误:{}", e.getMessage());
        }
    }


    /**
     * 获取单位时间内刷新时长和请求总时长
     *
     * @param timeUnit
     * @return
     */
    private long[] getIntervalAndQuota(String timeUnit) {
        if (timeUnit.equals(TimeUnit.SECONDS.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_SECOND_TTL};
        } else if (timeUnit.equals(TimeUnit.MINUTES.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_MINUTE_TTL};
        } else if (timeUnit.equals(TimeUnit.HOURS.name())) {
            return new long[]{SECONDS_IN_HOUR, PERIOD_HOUR_TTL};
        } else if (timeUnit.equals(TimeUnit.DAYS.name())) {
            return new long[]{SECONDS_IN_DAY, PERIOD_DAY_TTL};
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

    public Flux<AccessAuthority> getAccessAuthorities() {
        return accessAuthorities;
    }

    public void setAccessAuthorities(Flux<AccessAuthority> accessAuthorities) {
        this.accessAuthorities = accessAuthorities;
    }

    public Flux<GatewayIpLimitApisDto> getIpBlacks() {
        return ipBlacks;
    }

    public void setIpBlacks(Flux<GatewayIpLimitApisDto> ipBlacks) {
        this.ipBlacks = ipBlacks;
    }

    public Flux<GatewayIpLimitApisDto> getIpWhites() {
        return ipWhites;
    }

    public void setIpWhites(Flux<GatewayIpLimitApisDto> ipWhites) {
        this.ipWhites = ipWhites;
    }

    public HashMap<String, Collection<ConfigAttribute>> getAllConfigAttributeCache() {
        return allConfigAttributeCache;
    }

    public void setAllConfigAttributeCache(HashMap<String, Collection<ConfigAttribute>> allConfigAttributeCache) {
        this.allConfigAttributeCache = allConfigAttributeCache;
    }
}
