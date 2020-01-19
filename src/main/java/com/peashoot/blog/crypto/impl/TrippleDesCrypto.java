package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
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
        byte[] src = original.getBytes(charset);
        //DESedeKeySpec会帮你生成24位秘钥，key可以是任意长度
        DESedeKeySpec spec = new DESedeKeySpec(cryptoKey.getBytes(charset));
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        SecretKey secretKey = factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] res = cipher.doFinal(src);
        //encodeBase64会对字符串3位一组自动补全，因而最后可能会出现 == 或者 =
        return new String(Base64.encodeBase64(res), charset);
    }

    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        //DESedeKeySpec会帮你生成24位秘钥，key可以是任意长度
        DESedeKeySpec spec = new DESedeKeySpec(cryptoKey.getBytes(charset));
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        SecretKey secretKey = factory.generateSecret(spec);
        // 使用密钥初始化，设置为解密模式
        Cipher cipher = Cipher.getInstance(cipherMode);
        // 执行操作
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(result, charset);
    }
}
