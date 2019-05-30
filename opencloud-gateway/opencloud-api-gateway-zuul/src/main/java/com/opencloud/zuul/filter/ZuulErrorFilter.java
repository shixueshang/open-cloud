package com.opencloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.opencloud.zuul.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * zuul错误响应过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class ZuulErrorFilter extends ZuulFilter {
    private AccessLogService accessLogService;

    public ZuulErrorFilter(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // 代理错误日志记录
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        accessLogService.sendLog(request, response,(Exception) ctx.getThrowable());
        return null;
    }
}
