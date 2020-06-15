package com.peashoot.blog.security.handle;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peashoot.blog.context.response.ApiResp;
import org.apache.commons.codec.Charsets;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.util.HashMap;

/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        // BasicErrorController 是spring boot的默认异常处理控制器，当有异常抛出时，会由该控制器返回内容
        // outputStream.println("阿西吧");
        ApiResp<Object> retResp = new ApiResp<>();
        retResp.setCode(HttpStatus.FORBIDDEN.value());
        retResp.setMessage("Login first! (๑╹◡╹)ﾉ”");
        String response = JSON.toJSONString(retResp);
        byte[] retBodyBytes = response.getBytes(Charsets.UTF_8);
        outputStream.write(retBodyBytes);
        outputStream.flush();
    }
}
