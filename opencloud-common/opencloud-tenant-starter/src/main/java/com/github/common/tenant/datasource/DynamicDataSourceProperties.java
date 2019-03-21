package com.github.common.tenant.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 *
 * @author: liuyadu
 * @date: 2019/3/20 12:55
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.db")
public class DynamicDataSourceProperties {
    Map<String, DataSourceProperties> datasource = new HashMap<>();

    public Map<String, DataSourceProperties> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DataSourceProperties> datasource) {
        this.datasource = datasource;
    }
}
