package com.opencloud.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Date;

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
        return chain.filter(exchange);
    }


}

