package com.github.lyd.gateway.provider.filter;

import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * zuul代理前置过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class ZuulResponseFilter extends ZuulFilter {

    @Autowired
    private GatewayAccessLogsService gatewayAccessLogsService;

    /**
     * 是否应该执行该过滤器，如果是false，则不执行该filter
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器类型
     * 顺序: pre ->routing -> post ,以上3个顺序出现异常时都可以触发error类型的filter
     */
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    /**
     * 同filterType类型中，order值越大，优先级越低
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
    }

    /**
     *
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        try {
            Map headers = ctx.getZuulRequestHeaders();
            String requestId = headers.get(ZuulRequestFilter.X_REQUEST_ID).toString();
            String requestPath = request.getRequestURI();
            int httpStatus = response.getStatus();
            Map<String, Object> msg = Maps.newHashMap();
            msg.put("accessId", requestId);
            msg.put("path", requestPath);
            msg.put("save", "update");
            msg.put("httpStatus", httpStatus);
            msg.put("responseTime", new Date());
            gatewayAccessLogsService.saveLogs(msg);
        } catch (Exception e) {
            log.error("修改访问日志异常:{}", e);
        }
        return null;
    }


}
