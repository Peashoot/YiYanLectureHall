package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.util.EncryptUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TrippleDesCrypto implements Crypto {
     public TrippleDesCrypto(@NotNull String cryptoKey) {
         this.cryptoKey = cryptoKey;
     }
    /**
     * 加解密密钥
     */
    private String cryptoKey;
    /**
     * 加解密模式
     */
    private final String cipherMode = "DESede/ECB/PKCS5Padding";
    @Override
    public String encrypt(String original, String charset) throws Exception {
        return EncryptUtils.trippleDESEncrypt(original, cryptoKey, cipherMode, charset);
    }

    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        return EncryptUtils.trippleDESDecrypt(encrypted, cryptoKey, cipherMode, charset);
    }
}
