package com.github.lyd.task.provider.listenter;

import com.github.lyd.common.utils.DateUtils;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.task.provider.service.feign.EmailRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

/**
 * 任务调度监听
 *
 * @author liuyadu
 */
@Slf4j
public class JobLogsListener implements JobListener {
    private EmailRemoteService emailRemoteService;

    public JobLogsListener(EmailRemoteService emailRemoteService) {
        this.emailRemoteService = emailRemoteService;
    }

    @Override
    public String getName() {
        return "JobLogsListener";
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
        JobDataMap dataMap = detail.getJobDataMap();
        String name = detail.getKey().getName();
        log.debug("==> jobWasExecuted key[{}] runTime[{}] exception=[{}]", detail.getKey(), job.getJobRunTime(), e.getMessage());
        String alarmMail = dataMap.getString("alarmMail");
        String targetClass = detail.getJobClass().getName();
        String description = detail.getDescription();
        if (e != null) {
            if (StringUtils.isNotBlank(alarmMail)) {
                String exception = StringUtils.getStackMsg(e);
                String title = String.format("[%s]任务执行异常-%s", name, DateUtils.formatDateTime(new Date()));
                try {
                    emailRemoteService.sendEmail(alarmMail, title, exception);
                } catch (Exception em) {
                    log.error("==> send alarmMail error:{}", em);
                }
            }
        }

    }
}