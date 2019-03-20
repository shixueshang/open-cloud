package com.github.lyd.gateway.provider.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.security.OpenAuthUser;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.WebUtils;
import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * zuul代理前置过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class ZuulRequestFilter extends ZuulFilter {

    public static final String PRE_REQUEST_ID = "pre_request_id";

    public static final String PRE_REQUEST_ID_CACHE_PREFIX = "pre_request_id:";

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private RedisTemplate redisTemplate;

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
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    /**
     *
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();
        try {
            Map headers = WebUtils.getHttpHeaders(request);
            Map data = WebUtils.getParameterMap(request);
            Object serviceId = ctx.get(FilterConstants.SERVICE_ID_KEY);
            String requestPath = request.getRequestURI();
            String method = request.getMethod();
            String ip = WebUtils.getIpAddr(request);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String serverIp = WebUtils.getLocalIpAddress();
            Long requestId = snowflakeIdGenerator.nextId();
            request.setAttribute(PRE_REQUEST_ID, String.valueOf(requestId));
            Map<String, Object> map = Maps.newHashMap();
            map.put("accessId", requestId);
            map.put("serviceId",serviceId);
            map.put("headers", JSONObject.toJSON(headers));
            map.put("path", requestPath);
            map.put("params", JSONObject.toJSON(data));
            map.put("ip", ip);
            map.put("method", method);
            map.put("requestTime", new Date());
            map.put("userAgent", userAgent);
            map.put("serverIp", serverIp);
            OpenAuthUser user = OpenHelper.getAuthUser();
            if (user != null) {
                user.getUserProfile().remove("authorities");
                map.put("authentication", JSONObject.toJSONString(user));
            }
            // 3分钟过期
            String key = ZuulRequestFilter.PRE_REQUEST_ID_CACHE_PREFIX + requestId;
            // 放入redis缓存
            redisTemplate.opsForValue().set(key, map, 3, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("访问日志异常:{}", e);
        }

        return null;
    }


}
