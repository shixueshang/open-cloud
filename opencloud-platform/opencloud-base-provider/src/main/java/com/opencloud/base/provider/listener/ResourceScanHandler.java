package com.opencloud.base.provider.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencloud.base.client.model.entity.BaseResourceApi;
import com.opencloud.base.provider.service.BaseResourceApiService;
import com.opencloud.common.constants.MqConstants;
import com.opencloud.common.security.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.Duration;
import java.util.Iterator;

/**
 * mq消息接收者
 *
 * @author liuyadu
 */
@Configuration
@Slf4j
public class ResourceScanHandler {
    @Autowired
    private BaseResourceApiService baseResourceApiService;
    @Autowired
    private OpenRestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    private final static String SCAN_API_RESOURCE_KEY_PREFIX = "scan_api_resource:";

    /**
     * 接收API资源扫描消息
     */
    @RabbitListener(queues = MqConstants.QUEUE_SCAN_API_RESOURCE)
    public void ScanApiResourceQueue(@Payload JSONObject resource) {
        try {
            String serviceId = resource.getString("application");
            String key = SCAN_API_RESOURCE_KEY_PREFIX + serviceId;
            Object object = redisTemplate.opsForValue().get(key);
            if (object != null) {
                // 3分钟内未失效,不再更新资源
                return;
            }
            JSONArray array = resource.getJSONArray("mapping");
            Iterator iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                try {
                    BaseResourceApi api = jsonObject.toJavaObject(BaseResourceApi.class);
                    BaseResourceApi save = baseResourceApiService.getApi(api.getApiCode());
                    if (save == null) {
                        api.setIsOpen(0);
                        api.setIsAuth(1);
                        api.setIsPersist(1);
                        baseResourceApiService.addApi(api);
                    } else {
                        api.setIsOpen(null);
                        api.setApiId(save.getApiId());
                        baseResourceApiService.updateApi(api);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("添加资源error:", e.getMessage());
                }
            }
            if (array != null && array.size() > 0) {
                redisTemplate.opsForValue().set(key, array.size(), Duration.ofMinutes(3));
                restTemplate.refreshGateway();
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }
}
