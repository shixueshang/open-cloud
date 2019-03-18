package com.github.lyd.gateway.provider.listener;

import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.utils.BeanConvertUtils;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;
import com.github.lyd.gateway.provider.mapper.GatewayLogsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

/**
 * mq消息接收者
 *
 * @author liuyadu
 */
@Configuration
@Slf4j
public class MessageHandler {

    @Autowired
    private GatewayLogsMapper gatewayLogsMapper;

    /**
     * 接收访问日志
     *
     * @param access
     */
    @RabbitListener(queues = MqConstants.QUEUE_ACCESS_LOGS)
    public void accessLogsQueue(@Payload Map access) {
        try {
            if (access != null) {
                GatewayAccessLogs gatewayAccessLogs = BeanConvertUtils.mapToObject(access, GatewayAccessLogs.class);
                if (gatewayAccessLogs != null) {
                    if ("insert".equals(access.get("save"))) {
                        gatewayLogsMapper.insertSelective(gatewayAccessLogs);
                    } else {
                        GatewayAccessLogs logs = gatewayLogsMapper.selectByPrimaryKey(gatewayAccessLogs.getAccessId());
                        if (logs != null) {
                            gatewayAccessLogs.setUseTime(gatewayAccessLogs.getResponseTime().getTime() - logs.getRequestTime().getTime());
                            gatewayLogsMapper.updateByPrimaryKeySelective(gatewayAccessLogs);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }
}
