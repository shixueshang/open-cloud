package com.opencloud.common.exception;

import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义签名错误处理器
 *
 * @author liuyadu
 */
@Slf4j
public class OpenSignatureDeniedHandler implements SignatureDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       Exception exception) throws IOException, ServletException {
        ResultBody resultBody = OpenGlobalExceptionHandler.resolveException(exception, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        WebUtils.writeJson(response, resultBody);
    }
}