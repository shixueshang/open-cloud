package com.opencloud.task.provider.configuration;

import com.opencloud.common.configuration.CommonProperties;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAccessDeniedHandler;
import com.opencloud.common.exception.OpenAuthenticationEntryPoint;
import com.opencloud.common.security.OpenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * oauth2资源服务器配置
 * 如过新建一个资源服务器，直接复制该类到项目中.
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private CommonProperties properties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建ResourceServerTokenServices服务,这里是为了支持自定义用户信息转换器
        resources.tokenServices(OpenHelper.buildResourceServerTokenServices(properties));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // 内部访问直接放行
                .antMatchers("/**").permitAll()
                // 只有拥有actuator权限可执行远程端点
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyAuthority(CommonConstants.AUTHORITY_ACTUATOR)
                .anyRequest().authenticated()
                .and()
                //认证鉴权错误处理
                .exceptionHandling()
                .accessDeniedHandler(new OpenAccessDeniedHandler())
                .authenticationEntryPoint(new OpenAuthenticationEntryPoint())
                .and()
                .csrf().disable();
    }

}

