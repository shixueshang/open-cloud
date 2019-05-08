package com.opencloud.common.exception;

import com.opencloud.common.constants.ResultEnum;
import com.opencloud.common.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

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
        ResultBody resultBody = resolveException(ex);
        resultBody.setPath(request.getRequestURI());
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
        ResultBody resultBody = resolveException(ex);
        resultBody.setPath(request.getRequestURI());
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
        ResultBody resultBody = resolveException(ex);
        resultBody.setPath(request.getRequestURI());
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
        ResultBody resultBody = resolveException(ex);
        resultBody.setPath(request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }


    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static ResultBody resolveException(Exception ex) {
        ResultEnum code = ResultEnum.ERROR;
        int httpStatus = HttpStatus.OK.value();
        if (ex instanceof AuthenticationException) {
            if (ex instanceof UsernameNotFoundException) {
                code = ResultEnum.USERNAME_NOT_FOUND;
            } else if (ex instanceof BadCredentialsException) {
                code = ResultEnum.BAD_CREDENTIALS;
            } else if (ex instanceof AccountExpiredException) {
                code = ResultEnum.ACCOUNT_EXPIRED;
            } else if (ex instanceof LockedException) {
                code = ResultEnum.ACCOUNT_LOCKED;
            } else if (ex instanceof DisabledException) {
                code = ResultEnum.ACCOUNT_DISABLED;
            } else if (ex instanceof CredentialsExpiredException) {
                code = ResultEnum.CREDENTIALS_EXPIRED;
            }else{
                code = ResultEnum.UNAUTHORIZED;
                httpStatus = HttpStatus.UNAUTHORIZED.value();
            }
        } else if (ex instanceof OAuth2Exception) {
            code = ResultEnum.UNAUTHORIZED;
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            if (ex instanceof InvalidClientException) {
                code = ResultEnum.INVALID_CLIENT;
            } else if (ex instanceof UnauthorizedClientException) {
                code = ResultEnum.UNAUTHORIZED_CLIENT;
            } else if (ex instanceof InvalidGrantException) {
                code = ResultEnum.INVALID_GRANT;
                if ("Bad credentials".equals(ex.getMessage())) {
                    code = ResultEnum.BAD_CREDENTIALS;
                    httpStatus = HttpStatus.OK.value();
                }
                if ("User is disabled".equals(ex.getMessage())) {
                    code = ResultEnum.ACCOUNT_DISABLED;
                    httpStatus = HttpStatus.OK.value();
                }
                if ("User account is locked".equals(ex.getMessage())) {
                    code = ResultEnum.ACCOUNT_LOCKED;
                    httpStatus = HttpStatus.OK.value();
                }
            } else if (ex instanceof InvalidScopeException) {
                code = ResultEnum.INVALID_SCOPE;
            } else if (ex instanceof InvalidTokenException) {
                code = ResultEnum.INVALID_TOKEN;
            } else if (ex instanceof InvalidRequestException) {
                code = ResultEnum.INVALID_REQUEST;
            } else if (ex instanceof RedirectMismatchException) {
                code = ResultEnum.REDIRECT_URI_MISMATCH;
            } else if (ex instanceof UnsupportedGrantTypeException) {
                code = ResultEnum.UNSUPPORTED_GRANT_TYPE;
            } else if (ex instanceof UnsupportedResponseTypeException) {
                code = ResultEnum.UNSUPPORTED_RESPONSE_TYPE;
            } else if (ex instanceof UserDeniedAuthorizationException) {
                code = ResultEnum.ACCESS_DENIED;
            } else {
                code = ResultEnum.INVALID_REQUEST;
            }
            if (code.equals(ResultEnum.ACCESS_DENIED)) {
                httpStatus = HttpStatus.FORBIDDEN.value();
            }
        } else if (ex instanceof OpenException) {
            code = ResultEnum.ERROR;
            httpStatus = HttpStatus.OK.value();
            if (ex instanceof OpenAlertException) {
                code = ResultEnum.ALERT;
            }
            if (ex instanceof OpenSignatureException) {
                code = ResultEnum.SIGNATURE_DENIED;
            }
        } else {
            code = ResultEnum.ERROR;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
            if (ex instanceof HttpMessageNotReadableException || ex instanceof TypeMismatchException || ex instanceof MissingServletRequestParameterException) {
                httpStatus = HttpStatus.BAD_REQUEST.value();
                code = ResultEnum.BAD_REQUEST;
            } else if (ex instanceof NoHandlerFoundException) {
                httpStatus = HttpStatus.NOT_FOUND.value();
                code = ResultEnum.NOT_FOUND;
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
                code = ResultEnum.METHOD_NOT_ALLOWED;
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                httpStatus = HttpStatus.BAD_REQUEST.value();
                code = ResultEnum.MEDIA_TYPE_NOT_ACCEPTABLE;
            } else if (ex instanceof MethodArgumentNotValidException) {
                BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
                code = ResultEnum.ALERT;
                return ResultBody.failed(code.getCode(), bindingResult.getFieldError().getDefaultMessage());
            } else if (ex instanceof IllegalArgumentException) {
                //参数错误
                code = ResultEnum.ALERT;
                httpStatus = HttpStatus.BAD_REQUEST.value();
            } else if (ex instanceof AccessDeniedException) {
                code = ResultEnum.ACCESS_DENIED;
                httpStatus = HttpStatus.FORBIDDEN.value();
            }
        }
        return buildBody(ex, code, null, httpStatus);
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
        String error = resultCode.getMessage();
        ResultBody resultBody = ResultBody.failed(code, message).setError(error).setPath(path).setHttpStatus(httpStatus);
        log.error("==> 错误解析:{}", resultBody);
        return resultBody;
    }

}
