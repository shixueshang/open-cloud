package com.github.lyd.task.provider.listenter;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 任务调度监听
 *
 * @author liuyadu
 */
@Slf4j
public class JobLogsListener implements JobListener {

    @Override
    public String getName() {
        return "TaskLogsListener";
    }


    /**
     * 调度前执行
     *
     * @param job
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext job) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext job) {
    }

    /**
     * 调度完成或异常时执行
     *
     * @param job
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext job, JobExecutionException e) {
        JobDetail detail = job.getJobDetail();
        log.debug("==> jobWasExecuted key[{}] runTime[{}] exception=[{}]", detail.getKey(), job.getJobRunTime(),e);
        String targetClass = detail.getJobClass().getName();
        String description = detail.getDescription();

    }
}