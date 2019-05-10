package com.opencloud.common.exception;

import com.opencloud.common.constants.ResultEnum;
import com.opencloud.common.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理器
 *
 * @author LYD
 * @date 2017/7/3
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class OpenExceptionHandler {


    /**
     * 统一异常处理
     * AuthenticationException
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({AuthenticationException.class})
    public static ResultBody authenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * OAuth2Exception
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({OAuth2Exception.class, InvalidTokenException.class})
    public static ResultBody oauth2Exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 自定义异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({OpenException.class})
    public static ResultBody openException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 其他异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public static ResultBody exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }


    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static ResultBody resolveException(Exception ex,String path) {
        ResultEnum code = ResultEnum.ERROR;
        int httpStatus = HttpStatus.OK.value();
        String superClassName = ex.getClass().getSuperclass().getName();
        String className = ex.getClass().getName();
        if (superClassName.contains("AuthenticationException")) {
            if (className.contains("UsernameNotFoundException")) {
                code = ResultEnum.USERNAME_NOT_FOUND;
            } else if (className.contains("BadCredentialsException")) {
                code = ResultEnum.BAD_CREDENTIALS;
            } else if (className.contains("AccountExpiredException")) {
                code = ResultEnum.ACCOUNT_EXPIRED;
            } else if (className.contains("LockedException")) {
                code = ResultEnum.ACCOUNT_LOCKED;
            } else if (className.contains("DisabledException")) {
                code = ResultEnum.ACCOUNT_DISABLED;
            } else if (className.contains("CredentialsExpiredException")) {
                code = ResultEnum.CREDENTIALS_EXPIRED;
            } else {
                code = ResultEnum.UNAUTHORIZED;
                httpStatus = HttpStatus.UNAUTHORIZED.value();
            }
        } else if (superClassName.contains("OAuth2Exception")) {
            code = ResultEnum.UNAUTHORIZED;
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            if (className.contains("InvalidClientException")) {
                code = ResultEnum.INVALID_CLIENT;
            } else if (className.contains("UnauthorizedClientException")) {
                code = ResultEnum.UNAUTHORIZED_CLIENT;
            } else if (className.contains("InvalidGrantException")) {
                code = ResultEnum.INVALID_GRANT;
                if ("Bad credentials".contains(ex.getMessage())) {
                    code = ResultEnum.BAD_CREDENTIALS;
                    httpStatus = HttpStatus.OK.value();
                }
                if ("User is disabled".contains(ex.getMessage())) {
                    code = ResultEnum.ACCOUNT_DISABLED;
                    httpStatus = HttpStatus.OK.value();
                }
                if ("User account is locked".contains(ex.getMessage())) {
                    code = ResultEnum.ACCOUNT_LOCKED;
                    httpStatus = HttpStatus.OK.value();
                }
            } else if (className.contains("InvalidScopeException")) {
                code = ResultEnum.INVALID_SCOPE;
            } else if (className.contains("InvalidTokenException")) {
                code = ResultEnum.INVALID_TOKEN;
            } else if (className.contains("InvalidRequestException")) {
                code = ResultEnum.INVALID_REQUEST;
            } else if (className.contains("RedirectMismatchException")) {
                code = ResultEnum.REDIRECT_URI_MISMATCH;
            } else if (className.contains("UnsupportedGrantTypeException")) {
                code = ResultEnum.UNSUPPORTED_GRANT_TYPE;
            } else if (className.contains("UnsupportedResponseTypeException")) {
                code = ResultEnum.UNSUPPORTED_RESPONSE_TYPE;
            } else if (className.contains("UserDeniedAuthorizationException")) {
                code = ResultEnum.ACCESS_DENIED;
            } else {
                code = ResultEnum.INVALID_REQUEST;
            }
            if (code.equals(ResultEnum.ACCESS_DENIED)) {
                httpStatus = HttpStatus.FORBIDDEN.value();
            }
        } else if (superClassName.contains("OpenException")) {
            code = ResultEnum.ERROR;
            httpStatus = HttpStatus.OK.value();
            if (className.contains("OpenAlertException")) {
                code = ResultEnum.ALERT;
            }
            if (className.contains("exception.OpenSignatureException")) {
                code = ResultEnum.SIGNATURE_DENIED;
            }
        } else {
            code = ResultEnum.ERROR;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
            if (className.contains("HttpMessageNotReadableException")
                    || className.contains("TypeMismatchException")
                    || className.contains("MissingServletRequestParameterException")) {
                httpStatus = HttpStatus.BAD_REQUEST.value();
                code = ResultEnum.BAD_REQUEST;
            } else if (className.contains("NoHandlerFoundException")) {
                httpStatus = HttpStatus.NOT_FOUND.value();
                code = ResultEnum.NOT_FOUND;
            } else if (className.contains("HttpRequestMethodNotSupportedException")) {
                httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
                code = ResultEnum.METHOD_NOT_ALLOWED;
            } else if (className.contains("HttpMediaTypeNotAcceptableException")) {
                httpStatus = HttpStatus.BAD_REQUEST.value();
                code = ResultEnum.MEDIA_TYPE_NOT_ACCEPTABLE;
            } else if (className.contains("MethodArgumentNotValidException")) {
                BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
                code = ResultEnum.ALERT;
                return ResultBody.failed(code.getCode(), bindingResult.getFieldError().getDefaultMessage());
            } else if (className.contains("IllegalArgumentException")) {
                //参数错误
                code = ResultEnum.ALERT;
                httpStatus = HttpStatus.BAD_REQUEST.value();
            } else if (className.contains("AccessDeniedException")) {
                code = ResultEnum.ACCESS_DENIED;
                httpStatus = HttpStatus.FORBIDDEN.value();
            }
        }
        return buildBody(ex, code, path, httpStatus);
    }

    /**
     * 构建返回结果对象
     *
     * @param exception
     * @return
     */
    private static ResultBody buildBody(Exception exception, ResultEnum resultCode, String path, int httpStatus) {
        String message = exception.getMessage();
        if (resultCode == null) {
            resultCode = ResultEnum.ERROR;
        }
        int code = resultCode.getCode();
        exception.printStackTrace();
        String error = resultCode.getMessage();
        ResultBody resultBody = ResultBody.failed(code, message).setError(error).setPath(path).setHttpStatus(httpStatus);
        log.error("==> 错误解析:{}", resultBody);
        return resultBody;
    }

}
