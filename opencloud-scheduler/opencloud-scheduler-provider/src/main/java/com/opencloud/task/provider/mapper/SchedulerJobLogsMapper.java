package com.opencloud.task.provider.mapper;

import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.task.client.model.entity.SchedulerJobLogs;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerJobLogsMapper extends CrudMapper<SchedulerJobLogs> {
}
