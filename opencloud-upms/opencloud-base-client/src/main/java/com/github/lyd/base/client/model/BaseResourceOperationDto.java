package com.github.lyd.base.client.model;

import com.github.lyd.base.client.model.entity.BaseResourceOperation;

/**
 * 系统资源-功能操作
 * @author liuyadu
 */
public class BaseResourceOperationDto extends BaseResourceOperation {
    private static final long serialVersionUID = -691740581827186502L;
    /**
     * 资源路径
     */
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
