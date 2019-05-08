package com.opencloud.api.gateway.service.feign;

import com.opencloud.base.client.api.BaseAppRemoteApi;
import com.opencloud.base.client.constants.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:49
 * @description:
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVICE)
public interface BaseAppRemoteService extends BaseAppRemoteApi {
/*    *//**
     *
     *
     * 1.获取请求信息
     *
     *//*
    Route gatewayUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    URI uri = gatewayUrl.getUri();
    ServerHttpRequest request = (ServerHttpRequest)exchange.getRequest();
    HttpHeaders header = request.getHeaders();
    ServerHttpRequest.Builder mutate = request.mutate();

    String rawPath = request.getURI().toString();
    String path = request.getPath().value();
    String method = request.getMethodValue();
    String token = header.getFirst(JwtUtil.HEADER_AUTH);
    String instance = uri.getAuthority();

        System.out.println("*********************1.请求信息*******************");
        System.out.println("RawPath:" + rawPath);
        System.out.println("path:" + path);
        System.out.println("method:" + method);
        System.out.println("token:" + token);
        System.out.println("instance:" + instance);
        System.out.println("****************************************");*/

}
