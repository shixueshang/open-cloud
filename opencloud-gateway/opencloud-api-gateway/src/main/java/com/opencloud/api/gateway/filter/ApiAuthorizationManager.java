package com.opencloud.api.gateway.filter;

import com.opencloud.api.gateway.configuration.ApiProperties;
import com.opencloud.api.gateway.locator.ApiResourceLocator;
import com.opencloud.api.gateway.util.matcher.IpAddressMatcher;
import com.opencloud.base.client.model.AccessAuthority;
import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.constants.ResultEnum;
import com.opencloud.common.security.Authority;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * 自定义动态访问控制
 *
 * @author liuyadu
 */
@Slf4j
@Component
public class ApiAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private ApiResourceLocator accessLocator;

    private ApiProperties apiGatewayProperties;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    private Set<String> permitAll = new HashSet<>();

    private Set<String> authorityIgnores = new HashSet<>();


    public ApiAuthorizationManager(ApiResourceLocator accessLocator, ApiProperties apiGatewayProperties) {
        this.accessLocator = accessLocator;
        this.apiGatewayProperties = apiGatewayProperties;
        if (apiGatewayProperties != null) {
            if (apiGatewayProperties.getPermitAll() != null) {
                permitAll.addAll(apiGatewayProperties.getPermitAll());
            }
            if (apiGatewayProperties.getAuthorityIgnores() != null) {
                authorityIgnores.addAll(apiGatewayProperties.getAuthorityIgnores());
            }
        }
    }


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String requestPath = exchange.getRequest().getURI().getPath();
        if (!apiGatewayProperties.getAccessControl()) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 是否直接放行
        if (isPermitAll(requestPath)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 判断api是否需要认证
        boolean isAuth = isAuthAccess(requestPath);

        if (isAuth) {
            // 校验身份
           return authentication.map((a) -> {
                return new AuthorizationDecision(checkAuthorities(exchange, a, requestPath));
            });
        } else {
            return Mono.just(new AuthorizationDecision(true));
        }
    }


    private boolean isPermitAll(String requestPath) {
        Iterator<String> it = permitAll.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNoAuthorityAllow(String requestPath) {
        Iterator<String> it = authorityIgnores.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                //check if this uri can be access by anonymous
                //return
            }
            if (isNoAuthorityAllow(requestPath)) {
                // 认证通过,并且无需权限
                return true;
            }
            return mathAuthorities(exchange, authentication, requestPath);
        }
        return false;
    }

    public boolean mathAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
        Collection<ConfigAttribute> attributes = getAttributes(requestPath);
        if (authentication == null) {
            return false;
        } else {
            if (CommonConstants.ROOT.equals(authentication.getName())) {
                // 默认超级管理员账号,直接放行
                return true;
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator var6 = attributes.iterator();
            while (var6.hasNext()) {
                ConfigAttribute attribute = (ConfigAttribute) var6.next();
                Iterator var8 = authorities.iterator();
                while (var8.hasNext()) {
                    GrantedAuthority authority = (GrantedAuthority) var8.next();
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        if (authority instanceof Authority) {
                            Authority customer = (Authority) authority;
                            if (customer.getIsExpired() != null && customer.getIsExpired()) {
                                // 授权已过期
                                log.debug("==> access_denied:path={},message={}", requestPath, ResultEnum.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage());
                                exchange.getAttributes().put(CommonConstants.X_ACCESS_DENIED, ResultEnum.ACCESS_DENIED_AUTHORITY_EXPIRED);
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private Collection<ConfigAttribute> getAttributes(String requestPath) {
        // 匹配动态权限
        for (Iterator<String> iter = accessLocator.getAllConfigAttributeCache().keySet().iterator(); iter.hasNext(); ) {
            String url = iter.next();
            // 防止匹配错误 忽略/**
            if (!"/**".equals(url) && pathMatch.match(url, requestPath)) {
                // 返回匹配到权限
                return accessLocator.getAllConfigAttributeCache().get(url);
            }
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }

    private boolean isAuthAccess(String requestPath) {
        List<AccessAuthority> authorityList = accessLocator.getAccessAuthorities();
        if (authorityList != null) {
            for (AccessAuthority auth : authorityList) {
                String fullPath = auth.getPath();
                Boolean isAuth = auth.getIsAuth() != null && auth.getIsAuth().equals(1) ? true : false;
                // 需认证,返回true
                if (StringUtils.isNotBlank(fullPath) && pathMatch.match(fullPath, requestPath) && isAuth) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean matchIpBlacklist(String requestPath, String remoteIpAddress) {
        List<GatewayIpLimitApisDto> blackList = accessLocator.getIpBlacks();
        if (blackList != null) {
            for (GatewayIpLimitApisDto api : blackList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    if (matchIp(api.getIpAddressSet(), remoteIpAddress)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public boolean[] matchIpWhiteList(String requestPath, String remoteIpAddress) {
        boolean hasWhiteList = false;
        boolean allow = false;
        List<GatewayIpLimitApisDto> whiteList = accessLocator.getIpWhites();
        if (whiteList != null) {
            for (GatewayIpLimitApisDto api : whiteList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    hasWhiteList = true;
                    allow = matchIp(api.getIpAddressSet(), remoteIpAddress);
                    break;
                }
            }
        }
        return new boolean[]{hasWhiteList, allow};
    }

    public boolean matchIp(Set<String> ips, String remoteIpAddress) {
        IpAddressMatcher ipAddressMatcher = null;
        for (String ip : ips) {
            try {
                ipAddressMatcher = new IpAddressMatcher(ip);
                if (ipAddressMatcher.matches(remoteIpAddress)) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }
}
