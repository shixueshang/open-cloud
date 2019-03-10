package com.github.lyd.base.client.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "gateway_rate_limit_api")
public class GatewayRateLimitApi implements Serializable {
    @Id
    @Column(name = "limit_id")
    private Long limitId;

    /**
     * 限制数量
     */
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 时间间隔(秒)
     */
    @Column(name = "api_id")
    private Long apiId;

    @Column(name = "service_id")
    private String serviceId;

    /**
     * 限流规则类型:url,origin,user
     */
    @Column(name = "limit_type")
    private String limitType;

    /**
     * 限流规则内容
     */
    @Column(name = "limit_value")
    private String limitValue;

    private static final long serialVersionUID = 1L;

    /**
     * @return limit_id
     */
    public Long getLimitId() {
        return limitId;
    }

    /**
     * @param limitId
     */
    public void setLimitId(Long limitId) {
        this.limitId = limitId;
    }

    /**
     * 获取限制数量
     *
     * @return policy_id - 限制数量
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * 设置限制数量
     *
     * @param policyId 限制数量
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * 获取时间间隔(秒)
     *
     * @return api_id - 时间间隔(秒)
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * 设置时间间隔(秒)
     *
     * @param apiId 时间间隔(秒)
     */
    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    /**
     * @return service_id
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    /**
     * 获取限流规则类型:url,origin,user
     *
     * @return limit_type - 限流规则类型:url,origin,user
     */
    public String getLimitType() {
        return limitType;
    }

    /**
     * 设置限流规则类型:url,origin,user
     *
     * @param limitType 限流规则类型:url,origin,user
     */
    public void setLimitType(String limitType) {
        this.limitType = limitType == null ? null : limitType.trim();
    }

    /**
     * 获取限流规则内容
     *
     * @return limit_value - 限流规则内容
     */
    public String getLimitValue() {
        return limitValue;
    }

    /**
     * 设置限流规则内容
     *
     * @param limitValue 限流规则内容
     */
    public void setLimitValue(String limitValue) {
        this.limitValue = limitValue == null ? null : limitValue.trim();
    }
}
