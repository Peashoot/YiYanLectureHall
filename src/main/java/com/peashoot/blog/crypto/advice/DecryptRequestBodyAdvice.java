package com.peashoot.blog.crypto.advice;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.crypto.util.CryptoInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

@ControllerAdvice
@ConditionalOnProperty(prefix = "spring.crypto.request.decrypt", name = "enabled" , havingValue = "true", matchIfMissing = true)
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Value("${peashoot.blog.http.context.charset:UTF-8}")
    private String charset = "UTF-8";

    @Value("${peashoot.blog.http.context.decrypt.enabled:true}")
    private boolean enabled = true;

    @Autowired
    @Qualifier("cryptEntity")
    private Crypto crypto;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if(enabled && CryptoInvoker.needDecrypt(parameter) ){
            return new DecryptHttpInputMessage(inputMessage , charset , crypto);
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
