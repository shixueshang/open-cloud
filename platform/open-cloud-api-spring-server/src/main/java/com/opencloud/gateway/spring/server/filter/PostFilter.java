package com.opencloud.gateway.spring.server.filter;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 解决
 java.lang.IllegalStateException: Only one connection receive subscriber allowed.
 实际上spring-cloud-gateway反向代理的原理是，首先读取原请求的数据，然后构造一个新的请求，将原请求的数据封装到新的请求中，然后再转发出去。
 然而我们在他封装之前读取了一次request body，而request body只能读取一次。因此就出现了上面的错误
 */
@Component
public class PostFilter extends AbstractNameValueGatewayFilterFactory {


    @Override
    public GatewayFilter apply(Consumer<NameValueConfig> consumer) {
        return null;
    }

    @Override
    public GatewayFilter apply(NameValueConfig nameValueConfig) {
        return (exchange, chain) -> {
            URI uri = exchange.getRequest().getURI();
            URI ex = UriComponentsBuilder.fromUri(uri).build(true).toUri();
            ServerHttpRequest request = exchange.getRequest().mutate().uri(ex).build();
            if("POST".equalsIgnoreCase(request.getMethodValue())){
                //判断是否为POST请求
                Flux<DataBuffer> body = request.getBody();
                AtomicReference<String> bodyRef = new AtomicReference<>();
                //缓存读取的request body信息
                body.subscribe(dataBuffer -> {
                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                    DataBufferUtils.release(dataBuffer);
                    bodyRef.set(charBuffer.toString());
                });//读取request body到缓存
                String bodyStr = bodyRef.get();
                //获取request body
                DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
                Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

                request = new ServerHttpRequestDecorator(request){
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return bodyFlux;
                    }
                };//封装我们的request
            }
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    protected DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public ServerHttpRequest.Builder mutate(ServerHttpRequest request) {
        return null;
    }

    @Override
    public ShortcutType shortcutType() {
        return null;
    }

    @Override
    public String shortcutFieldPrefix() {
        return null;
    }
}
