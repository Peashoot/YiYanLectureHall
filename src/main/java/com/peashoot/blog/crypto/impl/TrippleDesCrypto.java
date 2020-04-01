package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.util.EncryptUtils;
import lombok.Getter;
import lombok.Setter;

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
        return EncryptUtils.trippleDesEncrypt(original, cryptoKey, cipherMode, charset);
    }

    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        return EncryptUtils.trippleDesDecrypt(encrypted, cryptoKey, cipherMode, charset);
    }
}
