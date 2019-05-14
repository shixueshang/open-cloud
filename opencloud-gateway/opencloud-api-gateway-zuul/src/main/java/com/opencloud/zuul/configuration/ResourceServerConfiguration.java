package com.opencloud.zuul.configuration;

import com.opencloud.common.gen.SnowflakeIdGenerator;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.zuul.exception.JsonAccessDeniedHandler;
import com.opencloud.zuul.exception.JsonAuthenticationEntryPoint;
import com.opencloud.zuul.filter.ApiAuthorizationManager;
import com.opencloud.zuul.filter.IpCheckFilter;
import com.opencloud.zuul.filter.PreRequestFilter;
import com.opencloud.zuul.filter.SignatureFilter;
import com.opencloud.zuul.service.AccessLogService;
import com.opencloud.zuul.service.feign.BaseAppRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * oauth2资源服务器配置
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private ApiProperties apiProperties;
    @Autowired
    private BaseAppRemoteService baseAppRemoteService;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private ApiAuthorizationManager apiAccessManager;

    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建redis获取token,这里是为了支持自定义用户信息转换器
        resources.tokenServices(OpenHelper.buildRedisTokenServices(redisConnectionFactory));
        resources.expressionHandler(expressionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                // 动态访问控制
                .anyRequest().access("@apiAuthorizationManager.check(request,authentication)")
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler(accessLogService))
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint(accessLogService))
                .and()
                .csrf().disable();
        // 网关日志前置过滤器
        http.addFilterBefore(new PreRequestFilter(snowflakeIdGenerator, redisTemplate, accessLogService), AbstractPreAuthenticatedProcessingFilter.class);
        // 增加签名验证过滤器
        http.addFilterAfter(new SignatureFilter(baseAppRemoteService, apiProperties), AbstractPreAuthenticatedProcessingFilter.class);
        // 增加IP检测过滤器
        http.addFilterAfter(new IpCheckFilter(apiAccessManager, new JsonAccessDeniedHandler(accessLogService)), AbstractPreAuthenticatedProcessingFilter.class);
    }
}

