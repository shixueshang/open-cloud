package com.github.lyd.msg.provider.service.impl;

import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.msg.client.model.entity.NotifyHttpLogs;
import com.github.lyd.msg.provider.mapper.NotifyHttpLogsMapper;
import com.github.lyd.msg.provider.service.NotifyHttpLogsService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 异步通知日志接口
 *
 * @author: liuyadu
 * @date: 2019/2/13 14:39
 * @description:
 */
@Service
public class NotifyHttpLogsServiceImpl implements NotifyHttpLogsService {
    @Autowired
    private NotifyHttpLogsMapper notifyHttpLogsMapper;

    /**
     * 添加日志
     *
     * @param log
     */
    @Override
    public void addLog(NotifyHttpLogs log) {
        notifyHttpLogsMapper.insertSelective(log);
    }

    /**
     * 更细日志
     *
     * @param log
     */
    @Override
    public void modifyLog(NotifyHttpLogs log) {
        notifyHttpLogsMapper.updateByPrimaryKeySelective(log);
    }

    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    @Override
    public NotifyHttpLogs getLog(String logId) {
        return notifyHttpLogsMapper.selectByPrimaryKey(logId);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<NotifyHttpLogs> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        NotifyHttpLogs query =  pageParams.mapToObject(NotifyHttpLogs.class);
        ExampleBuilder builder = new ExampleBuilder(NotifyHttpLogs.class);
        Example example = builder.criteria()
                .andLikeRight("url",query.getUrl())
                .andEqualTo("type",query.getType())
                .andEqualTo("result",query.getResult())
                .end().build();
        example.orderBy("createTime").desc();
        List<NotifyHttpLogs> list = notifyHttpLogsMapper.selectByExample(example);
        return new PageList(list);
    }
}
