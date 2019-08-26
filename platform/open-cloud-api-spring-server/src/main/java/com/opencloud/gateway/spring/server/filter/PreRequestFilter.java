package com.opencloud.gateway.spring.server.filter;

import com.opencloud.common.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

/**
 * 请求前缀过滤器,增加请求时间
 *
 * @author liuyadu
 */
@Slf4j
public class PreRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 添加请求时间
        exchange.getAttributes().put("requestTime", new Date());
        // 添加自定义请求头
        String rid = UUID.randomUUID().toString();
        exchange.getRequest().getHeaders().set(FeignRequestInterceptor.X_REQUEST_ID, rid);
        exchange.getResponse().getHeaders().set(FeignRequestInterceptor.X_REQUEST_ID,rid);
        return chain.filter(exchange);
    }


}

