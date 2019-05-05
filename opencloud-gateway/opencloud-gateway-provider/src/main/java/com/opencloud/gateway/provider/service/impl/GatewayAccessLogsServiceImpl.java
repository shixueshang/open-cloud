package com.opencloud.gateway.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.constants.MqConstants;
import com.opencloud.common.mapper.ExampleBuilder;
import com.opencloud.common.model.PageList;
import com.opencloud.common.model.PageParams;
import com.opencloud.autoconfigure.security.OpenUser;
import com.opencloud.autoconfigure.security.OpenHelper;
import com.opencloud.common.utils.WebUtils;
import com.opencloud.gateway.client.constants.GatewayContants;
import com.opencloud.gateway.client.model.entity.GatewayAccessLogs;
import com.opencloud.gateway.provider.mapper.GatewayLogsMapper;
import com.opencloud.gateway.provider.service.GatewayAccessLogsService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private GatewayLogsMapper gatewayLogsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.application.name}")
    private String defaultServiceId;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @JsonIgnore
    private Set<String> ignores = new HashSet<>(Arrays.asList(new String[]{
            "/auth/oauth/check_token/**",
            "/gateway/access/logs/**"
    }));

    /**
     * 不记录日志
     *
     * @param requestPath
     * @return
     */
    public boolean isIgnore(String requestPath) {
        Iterator<String> iterator = ignores.iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            if (antPathMatcher.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存日志
     * @param request
     * @param response
     */
    @Override
    public void saveLogs(HttpServletRequest request, HttpServletResponse response) {
        try {
            int httpStatus = response.getStatus();
            String requestPath = request.getRequestURI();
            String method = request.getMethod();
            Map headers = WebUtils.getHttpHeaders(request);
            Map data = WebUtils.getParameterMap(request);
            Object serviceId = request.getAttribute(FilterConstants.SERVICE_ID_KEY);
            String ip = WebUtils.getIpAddr(request);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String serverIp = WebUtils.getLocalIpAddress();
            Object object = request.getAttribute(GatewayContants.PRE_REQUEST_ID);
            Object error = request.getAttribute(CommonConstants.X_ERROR_MESSAGE);
            if (isIgnore(requestPath)) {
                return;
            }
            if (object != null) {
                String requestId = object.toString();
                String key = GatewayContants.PRE_REQUEST_ID_CACHE_PREFIX + requestId;
                Object cache = redisTemplate.opsForValue().get(key);
                if (cache != null) {
                    Map<String, Object> map = (Map) cache;
                    map.put("accessId", requestId);
                    map.put("serviceId", serviceId==null?defaultServiceId:serviceId);
                    map.put("httpStatus", httpStatus);
                    map.put("headers", JSONObject.toJSON(headers));
                    map.put("path", requestPath);
                    map.put("params", JSONObject.toJSON(data));
                    map.put("ip", ip);
                    map.put("method", method);
                    map.put("userAgent", userAgent);
                    map.put("serverIp", serverIp);
                    map.put("responseTime", new Date());
                    map.put("error",error);
                    OpenUser user = OpenHelper.getUser();
                    if (user != null) {
                        map.put("authentication", JSONObject.toJSONString(user));
                    }
                    redisTemplate.delete(key);
                    amqpTemplate.convertAndSend(MqConstants.QUEUE_ACCESS_LOGS, map);
                }
            }
        }catch (Exception e){
            log.error("access logs save error:{}", e);
        }

    }


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<GatewayAccessLogs> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        GatewayAccessLogs query =  pageParams.mapToObject(GatewayAccessLogs.class);
        ExampleBuilder builder = new ExampleBuilder(GatewayAccessLogs.class);
        Example example = builder.criteria()
                .andLikeRight("path",query.getPath())
                .andEqualTo("ip",query.getIp())
                .andEqualTo("serverIp",query.getServerIp())
                .andEqualTo("serviceId",query.getServiceId())
                .end().build();
        example.orderBy("accessId").desc();
        List<GatewayAccessLogs> list = gatewayLogsMapper.selectByExample(example);
        return new PageList(list);
    }
}
