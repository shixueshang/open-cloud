package com.github.lyd.gateway.provider.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;
import com.github.lyd.gateway.provider.mapper.GatewayLogsMapper;
import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private GatewayLogsMapper gatewayLogsMapper;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @JsonIgnore
    private Set<String> ignores = new HashSet<>(Arrays.asList(new String[]{
            "/**/oauth/**",
            "/base/access/logs/**"
    }));

    /**
     * 不记录日志
     *
     * @param requestPath
     * @return
     */
    public boolean isIgnore(String requestPath) {
        Iterator<String> iterator = ignores.iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            if (antPathMatcher.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存日志
     *
     * @param map
     */
    @Override
    public void saveLogs(Map map) {
        String requestPath = map.get("path").toString();
        if (isIgnore(requestPath)) {
            return;
        }
        amqpTemplate.convertAndSend(MqConstants.QUEUE_ACCESS_LOGS, map);
    }


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<GatewayAccessLogs> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        GatewayAccessLogs query =  pageParams.mapToObject(GatewayAccessLogs.class);
        ExampleBuilder builder = new ExampleBuilder(GatewayAccessLogs.class);
        Example example = builder.criteria()
                .andLikeRight("path",query.getPath())
                .andEqualTo("ip",query.getIp())
                .andEqualTo("serverIp",query.getServerIp())
                .andEqualTo("serviceId",query.getServiceId())
                .end().build();
        example.orderBy("accessId").desc();
        List<GatewayAccessLogs> list = gatewayLogsMapper.selectByExample(example);
        return new PageList(list);
    }
}
