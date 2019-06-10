package com.opencloud.api.gateway.configuration;

import com.opencloud.api.gateway.exception.JsonAccessDeniedHandler;
import com.opencloud.api.gateway.exception.JsonAuthenticationEntryPoint;
import com.opencloud.api.gateway.filter.AccessLogFilter;
import com.opencloud.api.gateway.filter.AccessAuthorizationManager;
import com.opencloud.api.gateway.filter.PreCheckFilter;
import com.opencloud.api.gateway.filter.PreRequestFilter;
import com.opencloud.api.gateway.locator.ApiResourceLocator;
import com.opencloud.api.gateway.oauth2.RedisAuthenticationManager;
import com.opencloud.api.gateway.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 网关安全配置类
 *
 * @author: liuyadu
 * @date: 2019/5/8 18:45
 * @description:
 */
@Configuration
public class ResourceServerConfiguration {

    private static final String MAX_AGE = "18000L";

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ApiResourceLocator apiAccessLocator;
    @Autowired
    private ApiProperties apiGatewayProperties;
    @Autowired
    private AccessLogService accessLogService;

    /**
     * 跨域配置
     *
     * @return
     */
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }

            }
            return chain.filter(ctx);
        };
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        // 自定义oauth2 认证, 使用redis读取token,而非jwt方式
        JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint(accessLogService);
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler(accessLogService);
        AccessAuthorizationManager apiAuthorizationManager = new AccessAuthorizationManager(apiAccessLocator, apiGatewayProperties);
        AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(new RedisAuthenticationManager(new RedisTokenStore(redisConnectionFactory)));
        oauth2.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().access(apiAuthorizationManager)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint).and()
                // 前置过滤器 添加请求时间
                .addFilterAt(new PreRequestFilter(), SecurityWebFiltersOrder.FIRST)
                // 跨域过滤器
                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
                // IP访问限制过滤器
                .addFilterAt(new PreCheckFilter(apiAuthorizationManager, accessDeniedHandler), SecurityWebFiltersOrder.CSRF)
                // oauth2认证过滤器
                .addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION)
                // 日志过滤器放到最后
                .addFilterAt(new AccessLogFilter(accessLogService), SecurityWebFiltersOrder.LAST);
        return http.build();
    }
}
