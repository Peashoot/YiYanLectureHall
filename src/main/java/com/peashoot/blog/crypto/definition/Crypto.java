package com.peashoot.blog.crypto.definition;

public interface Crypto {
    /**
     * 加密方法
     * @param original 原文
     * @param charset 编码方式
     * @return 密文
     */
    String encrypt(String original, String charset) throws Exception;

    /**
     * 解密方法
     * @param encrypted 密文
     * @param charset 编码方式
     * @return 原文
     */
    String decrypt(String encrypted, String charset) throws Exception;
}
