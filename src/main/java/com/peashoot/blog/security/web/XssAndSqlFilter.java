package com.peashoot.blog.security.web;

import com.alibaba.fastjson.JSONObject;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.util.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class XssAndSqlFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = "GET";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = getBodyString(xssRequest.getReader());
            if (StringUtils.isNotNullOrEmpty(param)) {
                if (XssAndSqlHttpServletRequestWrapper.checkXSSAndSql(param)) {
                    writeRejectedResponse(response);
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
            writeRejectedResponse(response);
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    /**
     * 将拒绝请求的内容写入到响应中
     *
     * @param response 响应
     * @throws IOException IO读写异常
     */
    private void writeRejectedResponse(ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(HttpStatus.FORBIDDEN.value());
        resp.setMessage("Request contains some non-compliant characters.");
        out.write(JSONObject.toJSONString(resp));
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    /**
     * 获取request请求body中参数
     */
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;

    }

}
