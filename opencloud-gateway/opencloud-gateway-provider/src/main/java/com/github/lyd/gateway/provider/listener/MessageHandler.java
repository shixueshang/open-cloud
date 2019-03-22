package com.github.lyd.gateway.provider.listener;

import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.utils.BeanConvertUtils;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;
import com.github.lyd.gateway.provider.mapper.GatewayLogsMapper;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
     * 临时存放减少io
     */
    private byte[] bytes = null;


    public String getRegion(String ip) {
        try {
            //db
            InputStream inputStream = MessageHandler.class.getClassLoader().getResourceAsStream("data/ip2region.db");
            if (bytes == null) {
                bytes = read(inputStream);
            }

            if (bytes == null) {
                return null;
            }
            //查询算法
            int algorithm = DbSearcher.MEMORY_ALGORITYM; //B-tree
            //DbSearcher.BINARY_ALGORITHM //Binary
            //DbSearcher.MEMORY_ALGORITYM //Memory
            try {
                DbConfig config = new DbConfig();
                DbSearcher searcher = new DbSearcher(config, bytes);

                //define the method
                Method method = null;
                switch (algorithm) {
                    case DbSearcher.BTREE_ALGORITHM:
                        method = searcher.getClass().getMethod("btreeSearch", String.class);
                        break;
                    case DbSearcher.BINARY_ALGORITHM:
                        method = searcher.getClass().getMethod("binarySearch", String.class);
                        break;
                    case DbSearcher.MEMORY_ALGORITYM:
                        method = searcher.getClass().getMethod("memorySearch", String.class);
                        break;
                }

                DataBlock dataBlock = null;
                if (Util.isIpAddress(ip) == false) {
                    log.error("Error: Invalid ip address");
                }

                dataBlock = (DataBlock) method.invoke(searcher, ip);

                return dataBlock.getRegion();

            } catch (Exception e) {
                log.error("Error:{}", e);
            }
        } catch (Exception e) {
            log.error("Error:{}", e);
        }
        return null;
    }

    public static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 接收访问日志
     *
     * @param access
     */
    @RabbitListener(queues = MqConstants.QUEUE_ACCESS_LOGS)
    public void accessLogsQueue(@Payload Map access) {
        try {
            if (access != null) {
                GatewayAccessLogs logs = BeanConvertUtils.mapToObject(access, GatewayAccessLogs.class);
                if (logs != null && logs.getAccessId() != null) {
                    if (logs.getIp() != null) {
                        logs.setRegion(getRegion(logs.getIp()));
                    }
                    logs.setUseTime(logs.getResponseTime().getTime() - logs.getRequestTime().getTime());
                    gatewayLogsMapper.insertSelective(logs);
                }
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }
}
