package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5Crypto implements Crypto {
    @Override
    public String encrypt(String original, String charset) throws Exception {
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(original.getBytes(charset));
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
        return new BigInteger(1, md.digest()).toString(16);
    }

    @Override
    public String decrypt(String encrypted, String charset) throws Exception {
        throw new UnsupportedOperationException();
    }
}