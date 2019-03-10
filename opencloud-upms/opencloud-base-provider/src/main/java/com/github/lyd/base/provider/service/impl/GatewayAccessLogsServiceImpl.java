package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.model.entity.GatewayAccessLogs;
import com.github.lyd.base.provider.mapper.GatewayLogsMapper;
import com.github.lyd.base.provider.service.GatewayAccessLogsService;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {
    @Autowired
    private GatewayLogsMapper gatewayLogsMapper;


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
