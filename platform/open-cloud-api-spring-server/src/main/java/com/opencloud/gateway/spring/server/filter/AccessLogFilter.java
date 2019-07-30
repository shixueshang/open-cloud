package com.opencloud.gateway.spring.server.filter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.opencloud.gateway.spring.server.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 日志过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class AccessLogFilter implements WebFilter {

    private AccessLogService accessLogService;
    //将 List 数据以""分隔进行拼接
    private static Joiner joiner = Joiner.on("");

    public AccessLogFilter(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(
                            //解决返回体分段传输
                            fluxBody.buffer().map(dataBuffers -> {
                                List<String> list = Lists.newArrayList();
                                dataBuffers.forEach(dataBuffer -> {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);
                                    String responseData = new String(content, Charset.forName("UTF-8"));
                                    list.add(responseData);
                                });
                                String responseData = joiner.join(list);
                                byte[] uppedContent = new String(responseData.getBytes(), Charset.forName("UTF-8")).getBytes();
                                // 发送日志
                                accessLogService.sendLog(exchange, null);
                                return bufferFactory.wrap(uppedContent);
                            }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }


}

