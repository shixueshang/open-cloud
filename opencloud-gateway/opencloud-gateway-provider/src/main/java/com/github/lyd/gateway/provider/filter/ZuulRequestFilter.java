package com.github.lyd.gateway.provider.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.security.OpenAuthUser;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;

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
public class ZuulRequestFilter extends ZuulFilter {

    @Autowired
    private GatewayAccessLogsService gatewayAccessLogsService;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

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

        return FilterConstants.PRE_TYPE;
    }

    /**
     * 同filterType类型中，order值越大，优先级越低
     */
    @Override
    public int filterOrder() {
        return 0;
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
            Long requestId = snowflakeIdGenerator.nextId();
            ctx.addZuulRequestHeader("zuul-request-id", String.valueOf(requestId));
            Map headers = WebUtils.getHttpHeaders(request);
            Map data = WebUtils.getParameterMap(request);
            String path = request.getRequestURI();
            String method = request.getMethod();
            String ip = WebUtils.getIpAddr(request);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String serverIp = WebUtils.getLocalIpAddress();
            int httpStatus = response.getStatus();
            Map<String, Object> msg = Maps.newHashMap();
            Date requestTime = new Date();
            msg.put("accessId", requestId);
            msg.put("save", "insert");
            msg.put("headers", JSONObject.toJSON(headers));
            msg.put("path", path);
            msg.put("params", JSONObject.toJSON(data));
            msg.put("ip", ip);
            msg.put("method", method);
            msg.put("httpStatus", httpStatus);
            msg.put("requestTime", requestTime);
            msg.put("userAgent",userAgent);
            msg.put("serverIp",serverIp);
            OpenAuthUser user = OpenHelper.getAuthUser();
            if(user!=null){
                msg.put("authentication",JSONObject.toJSONString(user));
            }
            gatewayAccessLogsService.saveLogs(msg);
        } catch (Exception e) {
            log.error("添加访问日志异常:{}", e);
        }

        return null;
    }


}
