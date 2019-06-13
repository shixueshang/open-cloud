package com.opencloud.msg.client.api;

import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 推送通知
 *
 * @author woodev
 */
public interface HttpNotifyRemoteApi {

    /**
     * HTTP异步通知
     *
     * @param url    通知地址
     * @param type   通知业务类型
     * @param params json字符串
     * @return
     */
    @ApiOperation("HTTP异步通知")
    @PostMapping("/http/notify")
    ResultBody<String> sendHttpNotify(
            @RequestParam(value = "url", required = true) String url,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "params", required = false) String params
    ) throws Exception;
}
