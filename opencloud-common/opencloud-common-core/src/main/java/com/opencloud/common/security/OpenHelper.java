package com.opencloud.common.security;

import com.opencloud.common.configuration.CommonProperties;
import com.opencloud.common.utils.BeanConvertUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Map;

/**
 * 认证信息帮助类
 *
 * @author liuyadu
 */
public class OpenHelper {

    /**
     * 获取认证用户信息
     *
     * @return
     */
    public static OpenUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != null) {
            if (authentication.getPrincipal() instanceof OpenUser) {
                return (OpenUser) authentication.getPrincipal();
            }
            if (authentication.getPrincipal() instanceof Map) {
                return BeanConvertUtils.mapToObject((Map) authentication.getPrincipal(), OpenUser.class);
            }
        }
        return null;
    }

    /**
     * 是否拥有权限
     *
     * @param authority
     * @return
     */
    public static Boolean hasAuthority(String authority) {
        OpenUser auth = getUser();
        if (auth == null) {
            return false;
        }
        if (AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains(authority)) {
            return true;
        }
        return false;
    }

    /**
     * 构建token转换器
     *
     * @return
     */
    public static DefaultAccessTokenConverter buildAccessTokenConverter() {
        OpenUserConverter userAuthenticationConverter = new OpenUserConverter();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        return accessTokenConverter;
    }

    /**
     * 构建jwtToken转换器
     *
     * @param properties
     * @return
     */
    public static JwtAccessTokenConverter buildJwtTokenEnhancer(CommonProperties properties) throws Exception {
        JwtAccessTokenConverter converter = new OpenJwtAccessTokenEnhancer();
        converter.setSigningKey(properties.getJwtSigningKey());
        converter.afterPropertiesSet();
        return converter;
    }

    /**
     * 构建自定义远程Token服务类
     *
     * @param properties
     * @return
     */
    public static RemoteTokenServices buildRemoteTokenServices(ResourceServerProperties properties) {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = buildAccessTokenConverter();
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl(properties.getTokenInfoUri());
        services.setClientId(properties.getClientId());
        services.setClientSecret(properties.getClientSecret());
        services.setAccessTokenConverter(accessTokenConverter);
        return services;
    }

    /**
     * 构建资源服务器JwtToken服务类
     *
     * @param properties
     * @return
     */
    public static ResourceServerTokenServices buildResourceServerTokenServices(CommonProperties properties) throws Exception {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = buildAccessTokenConverter();
        OpenJwtTokenService tokenServices = new OpenJwtTokenService();
        // 这里的签名key 保持和认证中心一致
        JwtAccessTokenConverter converter = buildJwtTokenEnhancer(properties);
        JwtTokenStore jwtTokenStore = new JwtTokenStore(converter);
        tokenServices.setTokenStore(jwtTokenStore);
        tokenServices.setJwtAccessTokenConverter(converter);
        tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);
        return tokenServices;
    }
}
