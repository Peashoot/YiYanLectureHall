package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
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
     * @param str 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    @Override
    public String encrypt(String str, String charset) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKeyString);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(charset)));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    @Override
    public String decrypt(String str, String charset) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(charset));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyString);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
