package com.peashoot.blog.crypto.impl;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.util.EncryptUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Crypto implements Crypto {
    @Override
    public String encrypt(String original, String charset) {
        return EncryptUtils.md5Encrypt(original, charset);
    }

    @Override
    public String decrypt(String encrypted, String charset) {
        throw new UnsupportedOperationException();
    }
}
