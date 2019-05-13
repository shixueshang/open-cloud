package com.opencloud.api.gateway.configuration;

import com.opencloud.api.gateway.exception.JsonExceptionHandler;
import com.opencloud.api.gateway.locator.ApiAccessLocator;
import com.opencloud.api.gateway.locator.DbRouteDefinitionLocator;
import com.opencloud.api.gateway.service.feign.BaseAuthorityRemoteService;
import com.opencloud.api.gateway.service.feign.GatewayRemoteService;
import com.opencloud.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ApiProperties.class})
public class ApiConfiguration {


    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("bean [{}]", holder);
        return holder;
    }

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     *
     * @param viewResolversProvider
     * @param serverCodecConfigurer
     * @return
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {

        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler();
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        log.debug("Init Json Exception Handler Instead Default ErrorWebExceptionHandler Success");
        return jsonExceptionHandler;
    }

    @Bean
    @Primary
    public SwaggerProvider swaggerProvider(RouteDefinitionLocator routeDefinitionLocator) {
        return new SwaggerProvider(routeDefinitionLocator);
    }

    /**
     * 动态路由加载
     *
     * @return
     */
    @Bean
    public DbRouteDefinitionLocator dbRouteDefinitionLocator(JdbcTemplate jdbcTemplate) {
        return new DbRouteDefinitionLocator(jdbcTemplate);
    }

    /**
     * 动态路由加载
     *
     * @return
     */
    @Bean
    @Lazy
    public ApiAccessLocator apiAccessLocator(RouteDefinitionLocator routeDefinitionLocator, BaseAuthorityRemoteService baseAuthorityRemoteService, GatewayRemoteService gatewayRemoteService) {
        return new ApiAccessLocator(routeDefinitionLocator, baseAuthorityRemoteService, gatewayRemoteService);
    }
}
