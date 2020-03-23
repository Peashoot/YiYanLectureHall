package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.util.EncryptUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 加密类库
 */
@Getter
@Setter
public class RsaCrypto implements Crypto {
    public RsaCrypto() {
        try {
            genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    public RsaCrypto(String publicKey, String privateKey) {
        this.publicKeyString = publicKey;
        this.privateKeyString = privateKey;
    }

    /**
     * 公钥
     */
    private String publicKeyString;
    /**
     * 私钥
     */
    private String privateKeyString;

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
    }

    /**
     * RSA公钥加密
     *
     * @param original 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    @Override
    public String encrypt(String original, String charset) throws Exception {
        return EncryptUtils.rsaEncrypt(original, publicKeyString, charset);
    }

    /**
     * RSA私钥解密
     *
     * @param encrypted 加密字符串
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        return EncryptUtils.rsaDecrypt(encrypted, privateKeyString, charset);
    }
}
