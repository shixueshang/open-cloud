package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源-功能操作
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@TableName("base_resource_operation")
public class BaseResourceOperation implements Serializable {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 资源ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long operationId;

    /**
     * 资源编码
     */
    private String operationCode;

    /**
     * 资源名称
     */
    private String operationName;

    /**
     * 资源父节点
     */
    private Long menuId;

    /**
     * 优先级 越小越靠前
     */
    private Integer priority;

    /**
     * 资源描述
     */
    private String operationDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 获取资源ID
     *
     * @return operation_id - 资源ID
     */
    public Long getOperationId() {
        return operationId;
    }

    /**
     * 设置资源ID
     *
     * @param operationId 资源ID
     */
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    /**
     * 获取资源编码
     *
     * @return operation_code - 资源编码
     */
    public String getOperationCode() {
        return operationCode;
    }

    /**
     * 设置资源编码
     *
     * @param operationCode 资源编码
     */
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    /**
     * 获取资源名称
     *
     * @return operation_name - 资源名称
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * 设置资源名称
     *
     * @param operationName 资源名称
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * 获取资源父节点
     *
     * @return menu_id - 资源父节点
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置资源父节点
     *
     * @param menuId 资源父节点
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取优先级 越小越靠前
     *
     * @return priority - 优先级 越小越靠前
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级 越小越靠前
     *
     * @param priority 优先级 越小越靠前
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
