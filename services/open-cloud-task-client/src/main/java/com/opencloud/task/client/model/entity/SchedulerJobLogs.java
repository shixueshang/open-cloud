package com.opencloud.task.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyadu
 */
@TableName("scheduler_job_logs")
public class SchedulerJobLogs implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    private Long logId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 任务执行类
     */
    private String jobClass;

    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * 任务触发器
     */
    private String triggerClass;

    /**
     * 任务表达式
     */
    private String cronExpression;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 日志创建时间
     */
    private Date createTime;

    /**
     * 任务执行数据
     */
    private String jobData;

    /**
     * 异常
     */
    private String exception;

    /**
     * 状态：0-失败 1-成功
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

    /**
     * @return log_id
     */
    public Long getLogId() {
        return logId;
    }

    /**
     * @param logId
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    /**
     * 获取任务名称
     *
     * @return job_name - 任务名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名称
     *
     * @param jobName 任务名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 获取任务组名
     *
     * @return job_group - 任务组名
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置任务组名
     *
     * @param jobGroup 任务组名
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    /**
     * 获取任务执行类
     *
     * @return job_class - 任务执行类
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * 设置任务执行类
     *
     * @param jobClass 任务执行类
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass == null ? null : jobClass.trim();
    }

    /**
     * 获取任务描述
     *
     * @return job_description - 任务描述
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * 设置任务描述
     *
     * @param jobDescription 任务描述
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription == null ? null : jobDescription.trim();
    }

    /**
     * 获取任务触发器
     *
     * @return trigger_class - 任务触发器
     */
    public String getTriggerClass() {
        return triggerClass;
    }

    /**
     * 设置任务触发器
     *
     * @param triggerClass 任务触发器
     */
    public void setTriggerClass(String triggerClass) {
        this.triggerClass = triggerClass == null ? null : triggerClass.trim();
    }

    /**
     * 获取任务表达式
     *
     * @return cron_expression - 任务表达式
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置任务表达式
     *
     * @param cronExpression 任务表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 获取运行时间
     *
     * @return run_time - 运行时间
     */
    public Long getRunTime() {
        return runTime;
    }

    /**
     * 设置运行时间
     *
     * @param runTime 运行时间
     */
    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取日志创建时间
     *
     * @return create_time - 日志创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置日志创建时间
     *
     * @param createTime 日志创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取任务执行数据
     *
     * @return job_data - 任务执行数据
     */
    public String getJobData() {
        return jobData;
    }

    /**
     * 设置任务执行数据
     *
     * @param jobData 任务执行数据
     */
    public void setJobData(String jobData) {
        this.jobData = jobData == null ? null : jobData.trim();
    }

    /**
     * 获取异常
     *
     * @return exception - 异常
     */
    public String getException() {
        return exception;
    }

    /**
     * 设置异常
     *
     * @param exception 异常
     */
    public void setException(String exception) {
        this.exception = exception == null ? null : exception.trim();
    }

    /**
     * 获取状态：0-失败 1-成功
     *
     * @return status - 状态：0-失败 1-成功
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0-失败 1-成功
     *
     * @param status 状态：0-失败 1-成功
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
