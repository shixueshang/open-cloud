package com.github.lyd.gateway.provider.service.task;

import com.github.lyd.common.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ScheduledTask {

    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 定时刷新网关
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void process() {
        log.info("定时刷新网关{}",System.currentTimeMillis());
        openRestTemplate.refreshGateway();
    }
}
