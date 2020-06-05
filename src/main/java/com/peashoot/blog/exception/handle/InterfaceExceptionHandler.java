package com.peashoot.blog.exception.handle;

import com.peashoot.blog.context.response.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口异常处理
 *
 * @author peashoot
 */
@Slf4j
@RestControllerAdvice
public class InterfaceExceptionHandler implements HandlerExceptionResolver {
    /**
     * 系统抛出的没有处理过的异常
     *
     * @param request 请求上下文
     * @param e       异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResp<Object> defaultErrorHandler(HttpServletRequest request, Exception e) {
        //把错误输出到日志
        log.error("DefaultException Handler---Host: {} invokes url: {} ERROR: {}",
                request.getRemoteHost(), request.getRequestURL(), e.getMessage());
        return ApiResp.createErrorResp(e);
    }

    /**
     * 统一处理参数校验
     *
     * @param request 请求报文
     * @param e       异常信息
     * @return 处理后的响应报文
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiResp<String> validExceptionHandler(HttpServletRequest request, BindException e) {
        return validExceptionHandler(request, e.getBindingResult());
    }

    /**
     * 统一处理参数校验
     *
     * @param request 请求报文
     * @param e       异常信息
     * @return 处理后的响应报文
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiResp<String> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        return validExceptionHandler(request, e.getBindingResult());
    }

    /**
     * 统一处理参数校验
     *
     * @param request       请求报文
     * @param bindingResult 字段的错误信息
     * @return 处理后的响应报文
     */
    private ApiResp<String> validExceptionHandler(HttpServletRequest request, BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        log.error("DefaultException Handler---Host: {} invokes url: {} field: {} error message: {}",
                request.getRemoteHost(), request.getRequestURL(), fieldError.getField(), fieldError.getDefaultMessage());
        return ApiResp.createArgumentErrorResp(fieldError.getDefaultMessage());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
