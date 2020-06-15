package com.peashoot.blog.crypto.advice;

import com.alibaba.fastjson.JSON;
import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.crypto.util.CryptoInvoker;
import com.peashoot.blog.context.response.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@ConditionalOnProperty(prefix = "spring.crypto.response.encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${peashoot.blog.http.context.charset:UTF-8}")
    private String charset = "UTF-8";

    @Value("${peashoot.blog.http.context.encrypt.enabled:true}")
    private boolean enabled = true;

    @Autowired
    @Qualifier("cryptEntity")
    private Crypto crypto;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encrypt = enabled && CryptoInvoker.needEncrypt(returnType);

        if (!encrypt) {
            return body;
        }

        if (!(body instanceof ApiResp)) {
            return body;
        }

        //只针对ResponseMsg的data进行加密
        ApiResp retResp = (ApiResp) body;
        Object data = retResp.getData();
        if (null == data) {
            return body;
        }

        String original;
        Class<?> dataClass = data.getClass();
        if (dataClass.isPrimitive() || (data instanceof String)) {
            original = String.valueOf(data);
        } else {
            original = JSON.toJSONString(data);
        }
        String cipherText = "";
        try {
            cipherText = crypto.encrypt(original, charset);
        } catch (Exception ex) {
            log.error("An exception appeared when encrypt response:", ex);
        }
        retResp.setData(cipherText);
        return retResp;
    }
}
