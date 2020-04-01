package com.peashoot.blog.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptUtils {
    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 32位MD5加密
     *
     * @param original 原始字符串
     * @return 加密结果
     */
    public static String md5Encrypt(String original) {
        return md5Encrypt(original, CHARSET_UTF8);
    }

    /**
     * 16位MD5加密
     *
     * @param original 原始字符串
     * @return 加密结果
     */
    public static String md5EncryptLen16(String original) {
        return md5Encrypt(original).substring(8, 24);
    }

    /**
     * 32位MD5加密
     *
     * @param original 原始字符串
     * @param charset  编码方式
     * @return 加密结果
     */
    public static String md5Encrypt(String original, String charset) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(original.getBytes(charset));
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    /**
     * 16位MD5加密
     *
     * @param original 原始字符串
     * @param charset  编码方式
     * @return 加密结果
     */
    public static String mdEncryptLen16(String original, String charset) {
        return md5Encrypt(original, charset).substring(8, 24);
    }

    /**
     * AES加密
     *
     * @param original  原始字符串
     * @param cryptoKey 加密密钥
     * @return 加密后的结果
     */
    public static String aesEncrypt(String original, String cryptoKey) throws Exception {
        return aesEncrypt(original, cryptoKey, "AES/ECB/PKCS5Padding", CHARSET_UTF8);
    }

    /**
     * AES加密
     *
     * @param original   原始字符串
     * @param cryptoKey  加密密钥
     * @param cipherMode 加密模式
     * @param charset    编码方式
     * @return 加密后的结果
     */
    public static String aesEncrypt(String original, String cryptoKey, String cipherMode, String charset) throws Exception {
        byte[] raw = cryptoKey.getBytes(charset);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(original.getBytes(charset));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    /**
     * AES解密
     *
     * @param encrypted 带解密内容
     * @param cryptoKey 解密密钥
     * @return 解密后的内容
     */
    public static String aesDecrypt(String encrypted, String cryptoKey) throws Exception {
        return aesDecrypt(encrypted, cryptoKey, "AES/ECB/PKCS5Padding", CHARSET_UTF8);
    }

    /**
     * AES解密
     *
     * @param encrypted  带解密内容
     * @param cryptoKey  解密密钥
     * @param cipherMode 加密模式
     * @param charset    编码方式
     * @return 解密后的内容
     */
    public static String aesDecrypt(String encrypted, String cryptoKey, String cipherMode, String charset) throws Exception {
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

    /**
     * RSA加密
     *
     * @param original        原始字符串
     * @param publicKeyString 公钥
     * @return 加密后的内容
     */
    public static String rsaEncrypt(String original, String publicKeyString) throws Exception {
        return rsaEncrypt(original, publicKeyString, CHARSET_UTF8);
    }

    /**
     * RSA加密
     *
     * @param original        原始字符串
     * @param publicKeyString 公钥
     * @param charset         编码方式
     * @return 加密后的内容
     */
    public static String rsaEncrypt(String original, String publicKeyString, String charset) throws Exception {
        //base64编码的公钥
        byte[] decoded = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(publicKeyString);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(cipher.doFinal(original.getBytes(charset)));
    }

    /**
     * RSA解密
     *
     * @param encrypted        带解密内容
     * @param privateKeyString 解密私钥
     * @return 解密后的内容
     */
    public static String rsaDecrypt(String encrypted, String privateKeyString) throws Exception {
        return rsaDecrypt(encrypted, privateKeyString, CHARSET_UTF8);
    }

    /**
     * RSA解密
     *
     * @param encrypted        带解密内容
     * @param privateKeyString 解密私钥
     * @param charset          编码方式
     * @return 解密后的内容
     */
    public static String rsaDecrypt(String encrypted, String privateKeyString, String charset) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(encrypted.getBytes(charset));
        //base64编码的私钥
        byte[] decoded = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(privateKeyString);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 3DES加密
     *
     * @param original  原始字符串
     * @param cryptoKey 加密密钥
     * @return 加密后的内容
     */
    public static String trippleDesEncrypt(String original, String cryptoKey) throws Exception {
        return trippleDesEncrypt(original, cryptoKey, "DESede/ECB/PKCS5Padding", CHARSET_UTF8);
    }

    /**
     * 3DES加密
     *
     * @param original   原始字符串
     * @param cryptoKey  加密密钥
     * @param cipherMode 加密模式
     * @param charset    编码方式
     * @return 加密后的内容
     */
    public static String trippleDesEncrypt(String original, String cryptoKey, String cipherMode, String charset) throws Exception {
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

    /**
     * 3DES解密
     *
     * @param encrypted 待解密的内容
     * @param cryptoKey 解密密钥
     * @return 解密后的内容
     */
    public static String trippleDesDecrypt(String encrypted, String cryptoKey) throws Exception {
        return trippleDesDecrypt(encrypted, cryptoKey, "DESede/ECB/PKCS5Padding", CHARSET_UTF8);
    }

    /**
     * 3DES解密
     *
     * @param encrypted  待解密的内容
     * @param cryptoKey  解密密钥
     * @param cipherMode 解密模式
     * @param charset    编码方式
     * @return 解密后的内容
     */
    public static String trippleDesDecrypt(String encrypted, String cryptoKey, String cipherMode, String charset) throws Exception {
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
