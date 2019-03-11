package com.github.lyd.gateway.client.model.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "gateway_rate_limit")
public class GatewayRateLimit implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 流量限制
     */
    private Long limit;

    /**
     * 时间间隔
     */
    private Long interval;

    /**
     * 时间单位:second-秒,minute-分钟,hour-小时,day-天
     */
    @Column(name = "interval_unit")
    private String intervalUnit;

    @Column(name = "limit_desc")
    private String limitDesc;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取流量限制
     *
     * @return limit - 流量限制
     */
    public Long getLimit() {
        return limit;
    }

    /**
     * 设置流量限制
     *
     * @param limit 流量限制
     */
    public void setLimit(Long limit) {
        this.limit = limit;
    }

    /**
     * 获取时间间隔
     *
     * @return interval - 时间间隔
     */
    public Long getInterval() {
        return interval;
    }

    /**
     * 设置时间间隔
     *
     * @param interval 时间间隔
     */
    public void setInterval(Long interval) {
        this.interval = interval;
    }

    /**
     * 获取时间单位:second-秒,minute-分钟,hour-小时,day-天
     *
     * @return interval_unit - 时间单位:second-秒,minute-分钟,hour-小时,day-天
     */
    public String getIntervalUnit() {
        return intervalUnit;
    }

    /**
     * 设置时间单位:second-秒,minute-分钟,hour-小时,day-天
     *
     * @param intervalUnit 时间单位:second-秒,minute-分钟,hour-小时,day-天
     */
    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit == null ? null : intervalUnit.trim();
    }

    /**
     * @return limit_desc
     */
    public String getLimitDesc() {
        return limitDesc;
    }

    /**
     * @param limitDesc
     */
    public void setLimitDesc(String limitDesc) {
        this.limitDesc = limitDesc == null ? null : limitDesc.trim();
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
