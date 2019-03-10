package com.github.lyd.gateway.provider.configuration;

import com.github.lyd.common.configuration.GatewayProperties;
import com.github.lyd.common.constants.AuthorityConstants;
import com.github.lyd.common.exception.OpenAccessDeniedHandler;
import com.github.lyd.common.exception.OpenAuthenticationEntryPoint;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.ReflectionUtils;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.provider.filter.SignatureFilter;
import com.github.lyd.gateway.provider.locator.AccessLocator;
import com.github.lyd.gateway.provider.service.feign.BaseAppRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private ResourceServerProperties properties;
    @Autowired
    private AccessLocator accessLocator;
    @Autowired
    private GatewayProperties gatewayProperties;
    @Autowired
    private BaseAppRemoteService systemAppClient;
    @Autowired
    private RestTemplate restTemplate;

    SecurityExpressionHandler securityExpressionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建远程获取token,这里是为了支持自定义用户信息转换器
        resources.tokenServices(OpenHelper.buildRemoteTokenServices(properties));
        // 反射获取
        securityExpressionHandler = (SecurityExpressionHandler) ReflectionUtils.getFieldValue(resources, "expressionHandler");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // 直接放行的请求
                .antMatchers(
                        "/login/*",
                        "/logout",
                        "/oauth/*",
                        "/auth/login",
                        "/auth/logout",
                        "/auth/oauth/**").permitAll()
                // 匹配监控权限actuator可执行远程端点
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyAuthority(AuthorityConstants.AUTHORITY_ACTUATOR)
                // 自定义动态权限拦截,使用已经默认的FilterSecurityInterceptor对象,可以兼容默认表达式鉴权
                .anyRequest().access("@accessProcessor.access(request,authentication)")
                .anyRequest().authenticated()
                // SSO退出
                .and().logout().logoutSuccessHandler(new SsoLogoutSuccessHandler(gatewayProperties.getServerAddr() + "/auth/logout", restTemplate))
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new OpenAccessDeniedHandler())
                .authenticationEntryPoint(new OpenAuthenticationEntryPoint())
                .and()
                .csrf().disable();

        // 增加签名验证过滤器
        http.addFilterAfter(new SignatureFilter(systemAppClient, gatewayProperties), AbstractPreAuthenticatedProcessingFilter.class);
    }

    static class SsoLogoutSuccessHandler implements LogoutSuccessHandler {
        private String defaultTargetUrl;
        private RestTemplate restTemplate;

        public SsoLogoutSuccessHandler(String defaultTargetUrl, RestTemplate restTemplate) {
            this.defaultTargetUrl = defaultTargetUrl;
            this.restTemplate = restTemplate;
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            try {
                restTemplate.getForEntity(defaultTargetUrl, String.class);
            } catch (Exception e) {
                log.error("sso logout error:", e);
            }
            WebUtils.writeJson(response, ResultBody.success("退出成功", null));
        }
    }

}

