package com.github.lyd.task.provider.configuration;

import com.github.lyd.task.provider.listenter.JobLogsListener;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author liuyadu
 */
@Configuration
public class SchedulerConfiguration implements SchedulerFactoryBeanCustomizer {

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        //延时5秒启动
        schedulerFactoryBean.setStartupDelay(5);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 任务执行日志监听
        schedulerFactoryBean.setGlobalJobListeners(new JobLogsListener());
    }

}
