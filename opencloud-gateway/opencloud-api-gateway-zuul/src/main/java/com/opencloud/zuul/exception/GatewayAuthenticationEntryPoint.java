package com.opencloud.zuul.exception;

import com.opencloud.common.exception.OpenExceptionHandler;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.WebUtils;
import com.opencloud.zuul.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 网关认证异常处理,记录日志
 * @author liuyadu
 */
@Slf4j
public class GatewayAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private AccessLogService accessLogService;

    public GatewayAuthenticationEntryPoint(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        ResultBody responseData = OpenExceptionHandler.resolveException(exception, request, response);
        // 保存日志
        accessLogService.sendLog(request, response);
        WebUtils.writeJson(response, responseData);
    }
}
