package com.opencloud.admin.provider.configuration;

import com.opencloud.common.configuration.OpenCommonProperties;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAccessDeniedHandler;
import com.opencloud.common.exception.OpenAuthenticationEntryPoint;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
@Slf4j
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private OpenCommonProperties commonProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建redis获取token,这里是为了支持自定义用户信息转换器
        resources.tokenServices(OpenHelper.buildRedisTokenServices(redisConnectionFactory));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/login/token","/sign").permitAll()
                // 只有拥有actuator权限可执行远程端点
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyAuthority(CommonConstants.AUTHORITY_ACTUATOR)
                .anyRequest().authenticated()
                // SSO退出
                .and().logout().logoutSuccessHandler(new SsoLogoutSuccessHandler(commonProperties.getApiServerAddr() + "/auth/logout", restTemplate))
                .and()
                //认证鉴权错误处理
                .exceptionHandling()
                .accessDeniedHandler(new OpenAccessDeniedHandler())
                .authenticationEntryPoint(new OpenAuthenticationEntryPoint())
                .and()
                .csrf().disable();
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
                Map headerMap = WebUtils.getHttpHeaders(request);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setAll(headerMap);
                HttpEntity<MultiValueMap<String, Object>> requestObj = new HttpEntity(null, httpHeaders);
                restTemplate.postForObject(defaultTargetUrl, requestObj, String.class);
            } catch (Exception e) {
                log.error("sso logout error:", e);
            }
            WebUtils.writeJson(response, ResultBody.ok().msg("退出成功"));
        }
    }

}

