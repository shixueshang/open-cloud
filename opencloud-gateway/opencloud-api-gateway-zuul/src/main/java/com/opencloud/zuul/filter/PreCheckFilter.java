package com.opencloud.zuul.filter;

import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.common.constants.ResultEnum;
import com.opencloud.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * IP访问限制过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class PreCheckFilter extends OncePerRequestFilter {

    private AccessDeniedHandler accessDeniedHandler;

    private AccessAuthorizationManager apiAccessManager;

    public PreCheckFilter(AccessAuthorizationManager apiAccessManager, AccessDeniedHandler accessDeniedHandler) {
        this.apiAccessManager = apiAccessManager;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = apiAccessManager.getRequestPath(request);
        String remoteIpAddress = WebUtils.getRemoteAddress(request);
        AuthorityResource resource = apiAccessManager.getResource(requestPath);
        if(resource!=null){
            if("0".equals(resource.getIsOpen().toString())){
                // 未公开
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ResultEnum.ACCESS_DENIED_NOT_OPEN.getMessage()));
                return;
            }
            if ("0".equals(resource.getStatus().toString())) {
                // 禁用
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ResultEnum.ACCESS_DENIED_DISABLED.getMessage()));
                return;
            } else if ("2".equals(resource.getStatus().toString())) {
                // 维护中
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ResultEnum.ACCESS_DENIED_UPDATING.getMessage()));
                return;
            }
        }


        // 1.ip黑名单检测
        boolean deny = apiAccessManager.matchIpBlacklist(requestPath, remoteIpAddress);
        if (deny) {
            // 拒绝
            accessDeniedHandler.handle(request, response, new AccessDeniedException(ResultEnum.ACCESS_DENIED_BLACK_IP_LIMITED.getMessage()));
            return;
        }

        // 3.ip白名单检测
        boolean[] matchIpWhiteListResult = apiAccessManager.matchIpWhiteList(requestPath, remoteIpAddress);
        boolean hasWhiteList = matchIpWhiteListResult[0];
        boolean allow = matchIpWhiteListResult[1];

        if (hasWhiteList) {
            // 接口存在白名单限制
            if (!allow) {
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ResultEnum.ACCESS_DENIED_WHITE_IP_LIMITED.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
