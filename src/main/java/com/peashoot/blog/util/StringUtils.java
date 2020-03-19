package com.peashoot.blog.util;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    /**
     * 将密码和盐通过隔位拼接的方式串在一起
     * password 密码
     * salt 盐
     *
     * @return 拼接后的结果
     */
    public static String mixSaltWithPassword(@NotNull String password, @NotNull String salt) {
        int maxLength = Math.max(password.length(), salt.length());
        StringBuilder stud = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            stud.append(password.charAt(i));
            if (i < salt.length()) {
                stud.append(salt.charAt(i));
            }
        }
        return stud.toString();
    }

    /**
     * 将byte数组转成对应编码格式的字符串
     *
     * @param array   byte数组
     * @param length  实际数组长度
     * @param charset 编码格式
     * @return 对应编码格式的字符串
     * @throws UnsupportedEncodingException 不支持的编码格式异常
     */
    public static String convertByteArrayToString(@NotNull byte[] array, int length, String charset) throws UnsupportedEncodingException {
        return new String(array, 0, length, charset);
    }

    /**
     * 将byte数组转成对应编码格式的字符串
     *
     * @param array   byte数组
     * @param charset 编码格式
     * @return 对应编码格式的字符串
     * @throws UnsupportedEncodingException 不支持的编码格式异常
     */
    public static String convertByteArrayToString(@NotNull byte[] array, String charset) throws UnsupportedEncodingException {
        return new String(array, 0, array.length, charset);
    }

    /**
     * 将byte数组转成对应编码格式的字符串
     *
     * @param array byte数组
     * @return 对应编码格式的字符串
     * @throws UnsupportedEncodingException 不支持的编码格式异常
     */
    public static String convertByteArrayToString(@NotNull byte[] array) throws UnsupportedEncodingException {
        return new String(array, 0, array.length, "UTF-8");
    }

    /**
     * 检查字符串是否为空
     * @param needCheck 待检查字符串
     *
     * @return 是否为空
     */
    public static boolean isNullOrEmpty(String needCheck) {
        return needCheck == null || needCheck == "";
    }

    /**
     * 检查字符串是否不为空
     *
     * @param needCheck 待检查字符串
     * @return 是否不为空
     */
    public static boolean isNotNullOrEmpty(String needCheck) {
        return !isNullOrEmpty(needCheck);
    }

    /**
     * 检查字符串是否为空字符串
     *
     * @param needCheck 待检查字符串
     * @return 是否为空
     */
    public static boolean isNullOrWhitespace(String needCheck) {
        return isNullOrEmpty(needCheck) || needCheck.trim() == "";
    }
}
