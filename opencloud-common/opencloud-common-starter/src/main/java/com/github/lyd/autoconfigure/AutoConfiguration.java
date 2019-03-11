package com.github.lyd.autoconfigure;

import com.github.lyd.common.annotation.AnnotationScan;
import com.github.lyd.common.configuration.CommonProperties;
import com.github.lyd.common.configuration.IdGenProperties;
import com.github.lyd.common.exception.OpenExceptionHandler;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.health.DbHealthIndicator;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CommonProperties.class, IdGenProperties.class})
public class AutoConfiguration {

    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        log.info("初始化加密工具类:{}",encoder);
        return encoder;
    }


    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("初始化Spring上下文工具类:{}",holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenExceptionHandler.class)
    public OpenExceptionHandler exceptionHandler() {
        OpenExceptionHandler exceptionHandler = new OpenExceptionHandler();
        log.info("初始化全局异常处理器:{}", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IdGenProperties.class)
    public SnowflakeIdGenerator snowflakeIdWorker(IdGenProperties properties) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(properties.getWorkId(), properties.getCenterId());
        log.info("初始化SnowflakeId生成器:{},{}", properties, snowflakeIdGenerator);
        return snowflakeIdGenerator;
    }


    /**
     * 自定义注解扫描
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AnnotationScan.class)
    public AnnotationScan annotationScan(AmqpTemplate amqpTemplate) {
        AnnotationScan scan = new AnnotationScan(amqpTemplate);
        log.info("初始化自定义注解扫描类{}", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类
     *
     * @param commonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenRestTemplate.class)
    public OpenRestTemplate openRestTemplate(CommonProperties commonProperties) {
        OpenRestTemplate restTemplate = new OpenRestTemplate(commonProperties);
        log.info("初始化自定义请求工具类:{}", restTemplate);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator dbHealthIndicator() {
        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
        return dbHealthIndicator;
    }

}
