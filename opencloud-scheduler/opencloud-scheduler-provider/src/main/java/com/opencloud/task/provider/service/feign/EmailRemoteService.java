package com.opencloud.task.provider.service.feign;

import com.opencloud.msg.client.api.EmailRemoteApi;
import com.opencloud.msg.client.constatns.MsgConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: liuyadu
 * @date: 2019/4/1 12:57
 * @description:
 */
@Component
@FeignClient(value = MsgConstants.MSG_SERVICE)
public interface EmailRemoteService extends EmailRemoteApi {
}
