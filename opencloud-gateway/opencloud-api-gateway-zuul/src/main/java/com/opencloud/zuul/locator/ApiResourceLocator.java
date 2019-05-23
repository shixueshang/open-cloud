package com.opencloud.zuul.locator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.StringToMatchTypeConverter;
import com.opencloud.base.client.model.AccessAuthority;
import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.base.client.model.GatewayRateLimitApisDto;
import com.opencloud.common.event.GatewayRemoteRefreshRouteEvent;
import com.opencloud.zuul.service.feign.BaseAuthorityRemoteService;
import com.opencloud.zuul.service.feign.GatewayRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义动态权限加载器
 *
 * @author liuyadu
 */
@Slf4j
public class ApiResourceLocator implements ApplicationListener<GatewayRemoteRefreshRouteEvent> {
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

    /**
     * 权限列表
     */
    private HashMap<String, Collection<ConfigAttribute>> allConfigAttributes;
    /**
     * 权限列表
     */
    private List<AccessAuthority> accessAuthorities;

    /**
     * IP黑名单
     */
    private List<GatewayIpLimitApisDto> ipBlacks;

    /**
     * Ip白名单
     */
    private List<GatewayIpLimitApisDto> ipWhites;

    /**
     * 网关API接口流量控制列表
     */
    private List<GatewayRateLimitApisDto> rateLimitApis;

    private RateLimitProperties rateLimitProperties;
    private JdbcRouteLocator zuulRoutesLocator;
    private BaseAuthorityRemoteService baseAuthorityRemoteService;
    private GatewayRemoteService gatewayRemoteService;
    private StringToMatchTypeConverter converter;


    public ApiResourceLocator(JdbcRouteLocator zuulRoutesLocator, RateLimitProperties rateLimitProperties, BaseAuthorityRemoteService baseAuthorityRemoteService, GatewayRemoteService gatewayRemoteService) {
        this.zuulRoutesLocator = zuulRoutesLocator;
        this.rateLimitProperties = rateLimitProperties;
        this.baseAuthorityRemoteService = baseAuthorityRemoteService;
        this.gatewayRemoteService = gatewayRemoteService;
        this.converter = new StringToMatchTypeConverter();
    }

    /**
     * 刷新访问配置
     */
    public void refresh() {
        loadAuthority();
        loadIpBlacks();
        loadIpWhites();
        loadRateLimit();
    }

    /**
     * 获取路由后的地址
     *
     * @return
     */
    protected String getFullPath(String serviceId, String path) {
        List<Route> routes = zuulRoutesLocator.getRoutes();
        if (routes != null && !routes.isEmpty()) {
            for (Route route : routes) {
                // 服务ID相同
                if (route.getId().equals(serviceId)) {
                    return route.getPrefix().concat(path);
                }
            }
        }
        return path;
    }

    /**
     * 加载授权列表
     */
    public void loadAuthority() {
        allConfigAttributes = Maps.newHashMap();
        accessAuthorities = Lists.newArrayList();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        try {
            // 查询所有接口
            accessAuthorities = baseAuthorityRemoteService.getAccessAuthorityList().getData();
            if (accessAuthorities != null) {
                for (AccessAuthority item : accessAuthorities) {
                    String path = item.getPath();
                    if (path == null) {
                        continue;
                    }
                    String fullPath = getFullPath(item.getServiceId(), path);
                    item.setPath(fullPath);
                    array = allConfigAttributes.get(fullPath);
                    if (array == null) {
                        array = new ArrayList<>();
                    }
                    if (!array.contains(item.getAuthority())) {
                        cfg = new SecurityConfig(item.getAuthority());
                        array.add(cfg);
                    }
                    allConfigAttributes.put(fullPath, array);
                }
            }
            log.info("=============加载动态权限:{}==============", accessAuthorities.size());
        } catch (Exception e) {
            log.error("加载动态权限错误:{}", e.getMessage());
        }
    }

    /**
     * 加载IP黑名单
     */
    public void loadIpBlacks() {
        ipBlacks = Lists.newArrayList();
        try {
            ipBlacks = gatewayRemoteService.getApiBlackList().getData();
            if (ipBlacks != null) {
                for (GatewayIpLimitApisDto item : ipBlacks) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
            }
            log.info("=============加载IP黑名单:{}==============", ipBlacks.size());
        } catch (Exception e) {
            log.error("加载IP黑名单错误:{}", e.getMessage());
        }
    }

    /**
     * 加载IP白名单
     */
    public void loadIpWhites() {
        ipWhites = Lists.newArrayList();
        try {
            ipWhites = gatewayRemoteService.getApiWhiteList().getData();
            if (ipWhites != null) {
                for (GatewayIpLimitApisDto item : ipWhites) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
            }
            log.info("=============加载IP白名单:{}==============", ipWhites.size());
        } catch (Exception e) {
            log.error("加载IP白名单错误:{}", e.getMessage());
        }
    }

    /**
     * 加载限流配置
     * 1. 认证用户（Authenticated User）
     * 使用已认证的用户名（username）或'anonymous'
     * 2. 原始请求（Request Origin）
     * 使用系统用户的原始请求
     * 3. URL
     * 使用上游请求的地址
     * 4. 针对每个服务的全局配置
     * 该方式不会验证Request Origin，Authenticated User或URL
     * 使用该方式只需不设置‘type’参数即可
     *
     * @return
     */
    public void loadRateLimit() {
        LinkedHashMap<String, List<RateLimitProperties.Policy>> policysMap = Maps.newLinkedHashMap();
        //从application.properties中加载限流信息
        policysMap.putAll(rateLimitProperties.getPolicyList());
        //从db中加载限流信息
        policysMap.putAll(loadRateLimitPolicy());
        rateLimitProperties.setPolicyList(policysMap);
        log.info("=============加载动态限流:{}==============", rateLimitProperties.getPolicyList().size());
    }


    protected Map<String, List<RateLimitProperties.Policy>> loadRateLimitPolicy() {
        Map<String, List<RateLimitProperties.Policy>> policyMap = Maps.newLinkedHashMap();
        try {
            rateLimitApis = gatewayRemoteService.getApiRateLimitList().getData();
            if (rateLimitApis != null && rateLimitApis.size() > 0) {
                for (GatewayRateLimitApisDto item : rateLimitApis) {
                    List<RateLimitProperties.Policy> policyList = policyMap.get(item.getServiceId());
                    if (policyList == null) {
                        policyList = Lists.newArrayList();
                    }
                    RateLimitProperties.Policy policy = new RateLimitProperties.Policy();
                    long[] arry = getIntervalAndQuota(item.getIntervalUnit());
                    Long refreshInterval = arry[0];
                    Long quota = arry[1];
                    String url = getFullPath(item.getServiceId(), item.getPath());
                    item.setPath(url);
                    policy.setLimit(item.getLimit());
                    policy.setRefreshInterval(refreshInterval);
                    policy.setQuota(quota);
                    String type = "url=".concat(item.getPath());
                    RateLimitProperties.Policy.MatchType matchType = converter.convert(type);
                    policy.getType().add(matchType);
                    policyList.add(policy);
                    policyMap.put(item.getServiceId(), policyList);
                }
            }
        } catch (Exception e) {
            log.error("加载动态限流错误:{}", e.getMessage());
        }
        return policyMap;
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
            throw new java.lang.IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

    public List<AccessAuthority> getAccessAuthorities() {
        return accessAuthorities;
    }

    public void setAccessAuthorities(List<AccessAuthority> accessAuthorities) {
        this.accessAuthorities = accessAuthorities;
    }

    public List<GatewayIpLimitApisDto> getIpBlacks() {
        return ipBlacks;
    }

    public void setIpBlacks(List<GatewayIpLimitApisDto> ipBlacks) {
        this.ipBlacks = ipBlacks;
    }

    public List<GatewayIpLimitApisDto> getIpWhites() {
        return ipWhites;
    }

    public void setIpWhites(List<GatewayIpLimitApisDto> ipWhites) {
        this.ipWhites = ipWhites;
    }

    public List<GatewayRateLimitApisDto> getRateLimitApis() {
        return rateLimitApis;
    }

    public void setRateLimitApis(List<GatewayRateLimitApisDto> rateLimitApis) {
        this.rateLimitApis = rateLimitApis;
    }

    public HashMap<String, Collection<ConfigAttribute>> getAllConfigAttributes() {
        return allConfigAttributes;
    }

    public void setAllConfigAttributes(HashMap<String, Collection<ConfigAttribute>> allConfigAttributes) {
        this.allConfigAttributes = allConfigAttributes;
    }

    @Override
    public void onApplicationEvent(GatewayRemoteRefreshRouteEvent gatewayRemoteRefreshRouteEvent) {
        try {
            // 延迟3秒再刷新
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        refresh();
    }
}
