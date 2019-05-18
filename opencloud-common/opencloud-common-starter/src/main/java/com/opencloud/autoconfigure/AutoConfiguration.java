package com.opencloud.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.opencloud.common.annotation.AnnotationScan;
import com.opencloud.common.configuration.OpenCommonProperties;
import com.opencloud.common.configuration.OpenIdGenProperties;
import com.opencloud.common.exception.OpenExceptionHandler;
import com.opencloud.common.gen.SnowflakeIdGenerator;
import com.opencloud.common.health.DbHealthIndicator;
import com.opencloud.common.security.CustomResponseErrorHandler;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({OpenCommonProperties.class,  OpenIdGenProperties.class})
public class AutoConfiguration {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        log.info("bean [{}]",encoder);
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
        log.info("bean [{}]",holder);
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
        log.info("bean [{}]", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenIdGenProperties.class)
    public SnowflakeIdGenerator snowflakeIdWorker(OpenIdGenProperties properties) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(properties.getWorkId(), properties.getCenterId());
        log.info("bean [{}] properties [{}]", snowflakeIdGenerator,properties);
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
        log.info("bean [{}]", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类
     *
     * @param openCommonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenRestTemplate.class)
    public OpenRestTemplate openRestTemplate(OpenCommonProperties openCommonProperties) {
        OpenRestTemplate restTemplate = new OpenRestTemplate(openCommonProperties);
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        log.info("bean [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator dbHealthIndicator() {
        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
        return dbHealthIndicator;
    }
}
