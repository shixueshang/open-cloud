package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.constants.MqConstants;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

/**
 * @author liuyadu
 */
@Component
public class GatewayAccessLogsService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    public void  saveLogs(Map map){
        String path = map.get("path").toString();
        if(antPathMatcher.match("/**/oauth/**", path) || antPathMatcher.match("/base/access/logs/**",path)){
            return;
        }
        amqpTemplate.convertAndSend(MqConstants.QUEUE_ACCESS_LOGS, map);
    }
}
