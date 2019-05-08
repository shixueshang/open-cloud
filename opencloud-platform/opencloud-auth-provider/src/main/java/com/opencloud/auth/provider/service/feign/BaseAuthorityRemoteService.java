package com.opencloud.auth.provider.service.feign;

import com.opencloud.base.client.api.BaseAuthorityRemoteApi;
import com.opencloud.base.client.constants.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:49
 * @description:
 */
@FeignClient(value = BaseConstants.BASE_SERVICE)
public interface BaseAuthorityRemoteService extends BaseAuthorityRemoteApi {


}
