package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 系统操作资源-API关联表
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@TableName("base_resource_operation_api")
public class BaseResourceOperationApi implements Serializable {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 操作资源ID
     */
    private Long operationId;

    /**
     * 接口资源ID
     */
    private Long apiId;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
