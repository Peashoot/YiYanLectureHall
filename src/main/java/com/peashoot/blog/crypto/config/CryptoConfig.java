package com.peashoot.blog.crypto.config;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.crypto.impl.RsaCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {
    /**
     * 私钥
     */
    @Value("${peashoot.blog.http.content.decrypt.key}")
    private String privateKey;
    /**
     * 公钥
     */
    @Value("${peashoot.blog.http.content.encrypt.key}")
    private String publicKey;
    /**
     * 加密解密方式使用一样的
     */
    @Bean("cryptEntity")
    public Crypto getCryptEntity(){
        return new RsaCrypto(publicKey, privateKey);
    }
}
