package com.opencloud.api.gateway.configuration;

import com.opencloud.api.gateway.exception.JsonAccessDeniedHandler;
import com.opencloud.api.gateway.exception.JsonAuthenticationEntryPoint;
import com.opencloud.api.gateway.oauth2.RedisAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;

/**
 * 网关安全配置类
 *
 * @author: liuyadu
 * @date: 2019/5/8 18:45
 * @description:
 */
@Configuration
public class SecurityConfiguration {
    @Autowired
   private RedisConnectionFactory redisConnectionFactory;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint();
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler();
        AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(new RedisAuthenticationManager(new RedisTokenStore(redisConnectionFactory)));
        oauth2.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(
                        "/",
                        "/**/v2/api-docs",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui.html").permitAll()
                .anyExchange().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint).and().addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
