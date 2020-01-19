package com.peashoot.blog.error.handle;

import com.peashoot.blog.context.response.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class APIExceptionHandler {
    /**
     * 系统抛出的没有处理过的异常
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResp<Object>> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        //把错误输出到日志
        log.error("DefaultException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(), request.getRequestURL(), e.getMessage());
        return new ResponseEntity<ApiResp<Object>>(ApiResp.createErrorResp(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
