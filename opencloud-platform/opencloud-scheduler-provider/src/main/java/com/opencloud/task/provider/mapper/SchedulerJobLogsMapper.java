package com.opencloud.task.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.task.client.model.entity.SchedulerJobLogs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulerJobLogsMapper extends BaseMapper<SchedulerJobLogs> {
}
