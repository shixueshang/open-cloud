package com.opencloud.msg.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.common.model.PageParams;
import com.opencloud.msg.client.model.entity.NotifyHttpLogs;
import com.opencloud.msg.provider.mapper.NotifyHttpLogsMapper;
import com.opencloud.msg.provider.service.NotifyHttpLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        notifyHttpLogsMapper.insert(log);
    }

    /**
     * 更细日志
     *
     * @param log
     */
    @Override
    public void modifyLog(NotifyHttpLogs log) {
        notifyHttpLogsMapper.updateById(log);
    }

    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    @Override
    public NotifyHttpLogs getLog(String logId) {
        return notifyHttpLogsMapper.selectById(logId);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<NotifyHttpLogs> findListPage(PageParams pageParams) {
        NotifyHttpLogs query = pageParams.mapToObject(NotifyHttpLogs.class);
        QueryWrapper<NotifyHttpLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getUrl()), NotifyHttpLogs::getUrl, query.getUrl())
                .eq(ObjectUtils.isNotEmpty(query.getType()), NotifyHttpLogs::getType, query.getType())
                .eq(ObjectUtils.isNotEmpty(query.getResult()), NotifyHttpLogs::getResult, query.getResult());
        return notifyHttpLogsMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }
}
