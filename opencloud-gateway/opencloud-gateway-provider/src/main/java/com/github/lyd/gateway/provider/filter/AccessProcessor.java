package com.github.lyd.gateway.provider.filter;

import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.common.configuration.GatewayProperties;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.provider.locator.AccessLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 访问控制
 */
@Service
public class AccessProcessor {

    @Autowired
    private AccessLocator accessLocator;

    @Autowired
    private GatewayProperties gatewayProperties;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    /**
     * 是否准入
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean access(HttpServletRequest request, Authentication authentication) {
        if (!gatewayProperties.getAccessControl()) {
            return true;
        }
        String requestPath = getRequestPath(request);
        String remoteIpAddress = WebUtils.getIpAddr(request);

        // 1.ip黑名单控制
        boolean inIpBlacklist = inIpBlacklist(requestPath, remoteIpAddress);
        if (inIpBlacklist) {
            // 拒绝
            throw new AccessDeniedException("black_ip_limited");
        }
        // 2.ip白名单控制
        boolean[] inIpWhiteList = inIpWhiteList(requestPath, remoteIpAddress);
        boolean hasWhiteList = inIpWhiteList[0];
        boolean inWhite = inIpWhiteList[1];

        // 3.判断api是否需要认证
        boolean isAuthAccess = isAuthAccess(requestPath);

        // 白名单限制  ip检查未通过 拒绝
        // 白名单限制 ip检查通过  校验身份
        // 非白名单限制 检测权限
        if (hasWhiteList) {
            // 白名单限制
            if (inWhite) {
                // 白名单限制 ip检查通过
                if (!isAuthAccess) {
                    // 无需身份验证,允许
                    return true;
                } else {
                    // 校验身份
                    return checkAuthorities(authentication, requestPath);
                }
            } else {
                // 白名单限制 ip检查未通过 拒绝
                throw new AccessDeniedException("white_ip_limited");
            }

        } else {
            // 非白名单限制 校验身份
            return checkAuthorities(authentication, requestPath);
        }
    }


    private boolean checkAuthorities(Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {

            if (authentication instanceof AnonymousAuthenticationToken) {
                //check if this uri can be access by anonymous
                //return
            }
            return mathAuthorities(authentication, requestPath);
        }
        return false;
    }

    public boolean mathAuthorities(Authentication authentication, String requestPath) {
        Collection<ConfigAttribute> attributes = getAttributes(requestPath);
        if (authentication == null) {
            return false;
        } else {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator var6 = attributes.iterator();
            Iterator var8 = authorities.iterator();
            while (var6.hasNext()) {
                ConfigAttribute attribute = (ConfigAttribute) var6.next();
                while (var8.hasNext()) {
                    GrantedAuthority authority = (GrantedAuthority) var8.next();
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        if (authority instanceof OpenGrantedAuthority) {
                            OpenGrantedAuthority customer = (OpenGrantedAuthority) authority;
                            if (customer.getIsExpired()) {
                                // 授权已过期
                                throw new AccessDeniedException("authority_is_expired");
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
        for (Iterator<String> iter = accessLocator.getAllConfigAttribute().keySet().iterator(); iter.hasNext(); ) {
            String url = iter.next();
            if (pathMatch.match(url, requestPath)) {
                // 返回匹配到权限
                return accessLocator.getAllConfigAttribute().get(url);
            }
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }


    private boolean inIpBlacklist(String requestPath, String remoteIpAddress) {
        List<GatewayIpLimitApisDto> blackList = accessLocator.getIpBlackList();
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

    private boolean[] inIpWhiteList(String requestPath, String remoteIpAddress) {
        boolean hasWhiteList = false;
        boolean inWhite = false;
        List<GatewayIpLimitApisDto> whiteList = accessLocator.getIpWhiteList();
        if (whiteList != null) {
            for (GatewayIpLimitApisDto api : whiteList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    hasWhiteList = true;
                    inWhite = matchIp(api.getIpAddressSet(), remoteIpAddress);
                    break;
                }
            }
        }
        return new boolean[]{hasWhiteList, inWhite};
    }

    private boolean matchIp(Set<String> ips, String remoteIpAddress) {
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

    private boolean isAuthAccess(String requestPath) {
        List<BaseAuthorityDto> authorityList = accessLocator.getAuthorityList();
        if (authorityList != null) {
            for (BaseAuthorityDto auth : authorityList) {
                String fullPath = auth.getPath();
                Boolean isAuth = auth.getIsAuth();
                // 无需认证,返回true
                if (pathMatch.match(fullPath, requestPath) && !isAuth) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }
}
