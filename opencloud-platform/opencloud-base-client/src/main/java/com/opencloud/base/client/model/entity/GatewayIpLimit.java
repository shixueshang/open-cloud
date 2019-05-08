package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyadu
 */
@TableName("gateway_ip_limit")
public class GatewayIpLimit implements Serializable {
    /**
     * 策略ID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long policyId;

    /**
     * 策略名称
     */
    private String policyName;

    /**
     * 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    private Integer policyType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近一次修改时间
     */
    private Date updateTime;

    /**
     * ip地址/IP段:多个用隔开;最多10个
     */
    private String ipAddress;

    private static final long serialVersionUID = 1L;

    /**
     * 获取策略ID
     *
     * @return policy_id - 策略ID
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * 设置策略ID
     *
     * @param policyId 策略ID
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * 获取策略名称
     *
     * @return policy_name - 策略名称
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * 设置策略名称
     *
     * @param policyName 策略名称
     */
    public void setPolicyName(String policyName) {
        this.policyName = policyName == null ? null : policyName.trim();
    }

    /**
     * 获取策略类型:0-拒绝/黑名单 1-允许/白名单
     *
     * @return policy_type - 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    public Integer getPolicyType() {
        return policyType;
    }

    /**
     * 设置策略类型:0-拒绝/黑名单 1-允许/白名单
     *
     * @param policyType 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最近一次修改时间
     *
     * @return update_time - 最近一次修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取ip地址/IP段:多个用,隔开,最多20个
     *
     * @return ip_address - ip地址/IP段:多个用,隔开,最多20个
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 设置ip地址/IP段:多个用,隔开,最多20个
     *
     * @param ipAddress ip地址/IP段:多个用,隔开,最多20个
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }
}
