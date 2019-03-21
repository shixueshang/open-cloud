package com.github.common.tenant.configuration;

import com.github.common.tenant.datasource.DynamicDataSourceAspect;
import com.github.common.tenant.datasource.DynamicDataSourceContextHolder;
import com.github.common.tenant.datasource.DynamicDataSourceProperties;
import com.github.common.tenant.datasource.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2019/3/20 13:20
 * @description:
 */
@Configuration
@EnableConfigurationProperties(value = {DynamicDataSourceProperties.class,OpenTenantProperties.class})
public class DynamicDataSourceConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceConfiguration.class);


    @ConditionalOnProperty(value = "opencloud.tenant",matchIfMissing = true)
    @ConditionalOnMissingBean(DynamicDataSourceAspect.class)
    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(OpenTenantProperties openSaasProperties){
        logger.info("Current tenant is [{}]",openSaasProperties.getTenantId());
       return new DynamicDataSourceAspect(openSaasProperties);
    }

    @ConditionalOnProperty(value = "opencloud.db",matchIfMissing = true)
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(DataSourceProperties dataSourceProperties, DynamicDataSourceProperties openDataSourceProperties) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(2);
        if (dataSourceProperties != null) {
            // 默认数据源配置
            openDataSourceProperties.getDatasource().put("master", dataSourceProperties);
        }

        // 构建数据源
        Iterator<Map.Entry<String, DataSourceProperties>> it = openDataSourceProperties.getDatasource().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DataSourceProperties> entry = it.next();
            DataSourceProperties properties = entry.getValue();
            DataSourceBuilder builder = DataSourceBuilder.create().driverClassName(properties.getDriverClassName());
            builder.url(properties.getUrl());
            builder.username(properties.getUsername());
            builder.password(properties.getPassword());
            builder.type(properties.getType());
            DataSource dataSource = builder.build();
            dataSourceMap.put(entry.getKey() , dataSource);
        }
        // 设置master 为默认数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceMap.get("master"));
        // 可动态路由的数据源里装载了所有可以被路由的数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        // To put datasource keys into DataSourceContextHolder
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());
        return dynamicRoutingDataSource;
    }

}
