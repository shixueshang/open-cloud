package com.github.lyd.base.provider.service.feign;

import com.github.lyd.auth.client.api.ClientDetailsRemoteApi;
import com.github.lyd.auth.client.constants.AuthConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liuyadu
 */
@Component
@FeignClient(value = AuthConstants.AUTH_SERVICE)
public interface ClientDetailsClientRemote extends ClientDetailsRemoteApi {
}
