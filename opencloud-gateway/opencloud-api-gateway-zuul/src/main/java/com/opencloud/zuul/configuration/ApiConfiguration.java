package com.opencloud.zuul.configuration;

import com.google.common.collect.Lists;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.netflix.zuul.ZuulFilter;
import com.opencloud.zuul.endpoint.GatewayRefreshBusEndpoint;
import com.opencloud.zuul.event.GatewayRefreshRemoteListener;
import com.opencloud.zuul.filter.ZuulErrorFilter;
import com.opencloud.zuul.filter.ZuulResponseFilter;
import com.opencloud.zuul.locator.AccessLocator;
import com.opencloud.zuul.locator.DbRouteLocator;
import com.opencloud.zuul.service.AccessLogService;
import com.opencloud.zuul.service.feign.BaseAuthorityRemoteService;
import com.opencloud.zuul.service.feign.GatewayRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 自定义bus配置类
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ApiProperties.class})
public class ApiConfiguration {
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "Authorization";
    private static final Long MAX_AGE = 18000L;

    @Autowired
    private DbRouteLocator zuulRoutesLocator;
    @Autowired
    private RateLimitProperties rateLimitProperties;
    @Autowired
    private BaseAuthorityRemoteService baseAuthorityRemoteService;
    @Autowired
    private AccessLogService accessLogService;
    /**
     * 响应过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulResponseFilter() {
        return new ZuulResponseFilter(accessLogService);
    }

    /**
     * 错误过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulErrorFilter() {
        return new ZuulErrorFilter(accessLogService);
    }


    /**
     * 访问控制加载器

     * @return
     */
    @Bean
    public AccessLocator accessLocator(GatewayRemoteService gatewayRemoteService) {
        return new AccessLocator(zuulRoutesLocator,rateLimitProperties, baseAuthorityRemoteService, gatewayRemoteService);
    }

    /**
     * 初始化路由加载器
     *
     * @return
     */
    @Bean
    public DbRouteLocator zuulRouteLocator(ZuulProperties zuulProperties, ServerProperties serverProperties, GatewayRemoteService gatewayRemoteService) {
        zuulRoutesLocator = new DbRouteLocator(serverProperties.getServlet().getContextPath(), zuulProperties, gatewayRemoteService);
        log.info("ZuulRoutesLocator:{}", zuulRoutesLocator);
        return zuulRoutesLocator;
    }

    /**
     * 配置网关刷新bus监听
     *
     * @return
     */
    @Bean
    public GatewayRefreshRemoteListener gatewayRefreshRemoteListener(AccessLocator accessLocator) {
        GatewayRefreshRemoteListener rateLimitRefreshRemoteListener = new GatewayRefreshRemoteListener(zuulRoutesLocator, accessLocator);
        log.info("bean [{}]", rateLimitRefreshRemoteListener);
        return rateLimitRefreshRemoteListener;
    }

    /**
     * 配置网关刷新bus端点
     *
     * @param context
     * @param bus
     * @return
     */
    @Bean
    @ConditionalOnEnabledEndpoint
    @ConditionalOnClass({Endpoint.class})
    public GatewayRefreshBusEndpoint gatewayRefreshBusEndpoint(ApplicationContext context, BusProperties bus) {
        GatewayRefreshBusEndpoint endpoint = new GatewayRefreshBusEndpoint(context, bus.getId());
        log.info("bean [{}]", endpoint);
        return endpoint;
    }


    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Lists.newArrayList(ALLOWED_HEADERS.split(",")));
        config.setAllowedOrigins(Lists.newArrayList(ALLOWED_ORIGIN.split(",")));
        config.setAllowedMethods(Lists.newArrayList(ALLOWED_METHODS.split(",")));
        config.setMaxAge(MAX_AGE);
        config.addExposedHeader(ALLOWED_EXPOSE);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        //最大优先级,设置0不好使
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
