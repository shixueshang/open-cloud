package com.opencloud.autoconfigure;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liuyadu
 * @Order(101)排序很重要,否则多个WebSecurityConfigurerAdapter会报错
 */
@Order(101)
public class WebMvcConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/",
                "/error",
                "/static/**",
                "/**/v2/api-docs/**",
                "/**/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui.html",
                "/doc.html",
                "/favicon.ico");
    }

    /**
     * 资源处理器
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html","doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
