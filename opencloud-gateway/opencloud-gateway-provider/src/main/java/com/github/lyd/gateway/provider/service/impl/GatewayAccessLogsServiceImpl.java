package com.github.lyd.gateway.provider.service.impl;

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

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService{
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private GatewayLogsMapper gatewayLogsMapper;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    /**
     * 保存日志
     * @param map
     */
    @Override
    public void  saveLogs(Map map){
        String path = map.get("path").toString();
        if(antPathMatcher.match("/**/oauth/**", path) || antPathMatcher.match("/base/access/logs/**",path)){
            return;
        }
        amqpTemplate.convertAndSend(MqConstants.QUEUE_ACCESS_LOGS, map);
    }



    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    @Override
    public PageList<GatewayAccessLogs> findListPage(PageParams pageParams, String keyword) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        ExampleBuilder builder = new ExampleBuilder(GatewayAccessLogs.class);
        Example example = builder.criteria().end().build();
        example.orderBy("accessId").desc();
        List<GatewayAccessLogs> list = gatewayLogsMapper.selectByExample(example);
        return new PageList(list);
    }
}
