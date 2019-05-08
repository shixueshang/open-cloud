package com.opencloud.zuul.filter;

import com.google.common.collect.Maps;
import com.opencloud.common.gen.SnowflakeIdGenerator;
import com.opencloud.zuul.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyadu
 */
@Slf4j
public class PreRequestFilter extends OncePerRequestFilter {
    private SnowflakeIdGenerator snowflakeIdGenerator;
    private RedisTemplate redisTemplate;
    private AccessLogService accessLogService;

    public PreRequestFilter(SnowflakeIdGenerator snowflakeIdGenerator, RedisTemplate redisTemplate, AccessLogService accessLogService) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.redisTemplate = redisTemplate;
        this.accessLogService = accessLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.debug("==> PreFilter");
        Date start = new Date();
        log.debug("==> start [{}] path[{}]", start.getTime(),httpServletRequest.getRequestURI());
        try {
            Long requestId = snowflakeIdGenerator.nextId();
            httpServletRequest.setAttribute(AccessLogService.PRE_REQUEST_ID, String.valueOf(requestId));
            Map<String, Object> map = Maps.newHashMap();
            map.put("accessId", requestId);
            map.put("requestTime", new Date());
            // 3分钟过期
            String key = AccessLogService.PRE_REQUEST_ID_CACHE_PREFIX + requestId;
            // 放入redis缓存
            redisTemplate.opsForValue().set(key, map, 3, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("访问日志异常:{}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        Date end = new Date();
        log.debug("==> end [{}] use [{}] httpStatus=[{}]", end.getTime(), end.getTime() - start.getTime(),httpServletResponse.getStatus());
        if (httpServletResponse.getStatus() == 200) {
            accessLogService.sendLog(httpServletRequest,httpServletResponse);
        }
    }
}
