package com.github.common.tenant.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author: liuyadu
 * @date: 2019/3/20 12:55
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.tenant")
public class OpenTenantProperties {
    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 共享表名,不区分租户
     */
    private List<String> shareTables;

    /**
     * 当前模块表名,区分租户
     */
    private List<String> moduleTables;

    /**
     * 模块安装sql文件路径,可用于动态部署当前模块
     */
    private List<String> initSql;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getShareTables() {
        return shareTables;
    }

    public void setShareTables(List<String> shareTables) {
        this.shareTables = shareTables;
    }

    public List<String> getModuleTables() {
        return moduleTables;
    }

    public void setModuleTables(List<String> moduleTables) {
        this.moduleTables = moduleTables;
    }

    public List<String> getInitSql() {
        return initSql;
    }

    public void setInitSql(List<String> initSql) {
        this.initSql = initSql;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OpenTenantProperties{");
        sb.append("tenantId='").append(tenantId).append('\'');
        sb.append(", shareTables=").append(shareTables);
        sb.append(", moduleTables=").append(moduleTables);
        sb.append('}');
        return sb.toString();
    }
}
