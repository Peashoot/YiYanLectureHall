package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
@Setter
public class AesCrypto implements Crypto {
    public AesCrypto() {
        try {
            genKey();
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    public AesCrypto(@NotNull String cryptoKey) {
        this.cryptoKey = cryptoKey;
    }

    /**
     * 加解密密钥
     */
    private String cryptoKey;

    /**
     * "算法/模式/补码方式"
     */
    private String cipherMode = "AES/ECB/PKCS5Padding";

    public void genKey() throws NoSuchAlgorithmException {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //2.根据ecnodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        keygen.init(128);
        //3.产生原始对称密钥
        SecretKey original_key = keygen.generateKey();
        //4.获得原始对称密钥的字节数组
        byte[] raw = original_key.getEncoded();
        cryptoKey = new String(raw);
    }

    @Override
    public String encrypt(String original, String charset) throws Exception {
        byte[] raw = cryptoKey.getBytes(charset);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(original.getBytes(charset));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        byte[] raw = cryptoKey.getBytes(charset);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        //先用base64解密
        byte[] afterBase64Decrypt = new Base64().decode(encrypted);
        byte[] originalByte = cipher.doFinal(afterBase64Decrypt);
        String original = new String(originalByte, charset);
        return original;
    }
}
