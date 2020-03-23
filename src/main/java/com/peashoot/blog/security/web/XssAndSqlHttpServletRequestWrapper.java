package com.peashoot.blog.security.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static Map<String, Pattern> filterPatternCollection;

    static {
        filterPatternCollection = new HashMap<>();
        // anything between script tags
        filterPatternCollection.put("with_script_pattern", Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>",
                Pattern.CASE_INSENSITIVE));
        // anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
        filterPatternCollection.put("with_src_pattern", Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        // any lonesome </script> tag
        filterPatternCollection.put("alone_right_script_pattern", Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>",
                Pattern.CASE_INSENSITIVE));
        // any lonesome <script ...> tag
        filterPatternCollection.put("alone_left_script_pattern", Pattern.compile("<[\r\n| | ]*script(.*?)>",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        // eval(...) expressions
        filterPatternCollection.put("with_eval_pattern", Pattern.compile("eval\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        // e-xpression(...) expressions
        filterPatternCollection.put("with_e_x_pression_pattern", Pattern.compile("e-xpression\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        // javascript:... expressions
        filterPatternCollection.put("with_javascript_pattern", Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*",
                Pattern.CASE_INSENSITIVE));
        // vbscript:... expressions
        filterPatternCollection.put("with_vbscript_pattern", Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*",
                Pattern.CASE_INSENSITIVE));
        // onload= expressions
        filterPatternCollection.put("with_onload_pattern", Pattern.compile("onload(.*?)=",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    }

    HttpServletRequest orgRequest = null;
    private Map<String, String[]> parameterMap;
    /**
     * 用于保存读取body中数据
     */
    private final byte[] body;

    public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        orgRequest = request;
        parameterMap = request.getParameterMap();
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    // 重写几个HttpServletRequestWrapper中的方法

    /**
     * 获取所有参数名
     *
     * @return 返回所有参数名
     */
    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<String>(parameterMap.keySet());
        return vector.elements();
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss & sql过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0) {
            return null;
        } else {
            String value = results[0];
            if (value != null) {
                value = xssEncode(value);
            }
            return value;
        }
    }

    /**
     * 获取指定参数名的所有值的数组，如：checkbox的所有数据 接收数组变量 ，如checkobx类型
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0) {
            return null;
        } else {
            int length = results.length;
            for (int i = 0; i < length; i++) {
                results[i] = xssEncode(results[i]);
            }
            return results;
        }
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss & sql过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {

        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private static String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            s = stripXSSAndSql(s);
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    // 转义大于号
                    sb.append("＞");
                    break;
                case '<':
                    // 转义小于号
                    sb.append("＜");
                    break;
                // case '\'':
                // sb.append("＇");// 转义单引号
                // break;
                // case '\"':
                // sb.append("＂");// 转义双引号
                // break;
                case '&':
                    // 转义&
                    sb.append("＆");
                    break;
                case '#':
                    // 转义#
                    sb.append("＃");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssAndSqlHttpServletRequestWrapper) {
            return ((XssAndSqlHttpServletRequestWrapper) req).getOrgRequest();
        }

        return req;
    }

    /**
     * 防止xss跨脚本攻击（替换，根据实际情况调整）
     */

    public static String stripXSSAndSql(String value) {
        if (value != null) {
            for (Pattern pattern : filterPatternCollection.values()) {
                value = pattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

    public static boolean checkXSSAndSql(String value) {
        boolean flag = false;
        if (value != null) {
            for (Pattern pattern : filterPatternCollection.values()) {
                if (pattern.matcher(value).matches()) {
                    return true;
                }
            }
        }
        return flag;
    }

    public final boolean checkParameter() {
        Map<String, String[]> submitParams = new HashMap(parameterMap);
        Set<String> submitNames = submitParams.keySet();
        for (String submitName : submitNames) {
            Object submitValues = submitParams.get(submitName);
            if ((submitValues instanceof String)) {
                if (checkXSSAndSql((String) submitValues)) {
                    return true;
                }
            } else if ((submitValues instanceof String[])) {
                for (String submitValue : (String[]) submitValues) {
                    if (checkXSSAndSql(submitValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
                // TODO Auto-generated method stub

            }
        };
    }

}
