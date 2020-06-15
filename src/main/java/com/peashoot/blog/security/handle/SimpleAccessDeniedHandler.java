package com.peashoot.blog.security.handle;

import com.alibaba.fastjson.JSON;
import com.peashoot.blog.context.response.ApiResp;
import org.apache.commons.codec.Charsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决认证过的用户访问无权限资源时的异常
 */
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        // BasicErrorController 是spring boot的默认异常处理控制器，当有异常抛出时，会由该控制器返回内容
        // outputStream.println("阿西吧");
        ApiResp<Object> retResp = new ApiResp<>();
        retResp.setCode(HttpStatus.UNAUTHORIZED.value());
        retResp.setMessage("Confirm your permissions! ヾ(◍°∇°◍)ﾉﾞ");
        String response = JSON.toJSONString(retResp);
        byte[] retBodyBytes = response.getBytes(Charsets.UTF_8);
        outputStream.write(retBodyBytes);
        outputStream.flush();
    }
}
