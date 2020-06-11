package com.peashoot.blog.util;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    /**
     * null字符串
     */
    public static final String NULL = "null";

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
    static String convertByteArrayToString(@NotNull byte[] array, int length, String charset) throws UnsupportedEncodingException {
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
        return new String(array, 0, array.length, StandardCharsets.UTF_8);
    }

    /**
     * 检查字符串是否为空
     *
     * @param needCheck 待检查字符串
     * @return 是否为空
     */
    public static boolean isNullOrEmpty(String needCheck) {
        return needCheck == null || EMPTY.equals(needCheck);
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
        return isNullOrEmpty(needCheck) || EMPTY.equals(needCheck.trim());
    }

    /**
     * 对特殊字符进行转义
     *
     * @param original 原始字符串
     * @return 转移后的字符串
     */
    public static String getSpecialEscape(@NotNull String original) {
        return original.replace(":", "\\:")
                .replace(":", "\\;")
                .replace("null", "<null>");
    }

    /**
     * 将异常的调用堆栈保存到字符串中
     *
     * @param e 异常
     * @return 堆栈字符串
     */
    public static String getStackTraceString(@NotNull Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    /**
     * 去除尾随字符串
     *
     * @param oriStr 原始字符串
     * @param suffix 后缀
     * @return 截取后的字符串
     */
    public static String trimEnd(@NotNull String oriStr, @NotNull String suffix) {
        if (suffix.equals(EMPTY)) {
            return oriStr;
        }
        int lastIndex;
        while ((lastIndex = oriStr.lastIndexOf(suffix)) == oriStr.length()) {
            oriStr = oriStr.substring(0, lastIndex);
        }
        return oriStr;
    }

    /**
     * 去除前导字符串
     * @param oriStr 原始字符串
     * @param prefix 前缀
     * @return 截取后的字符串
     */
    public static String trimBegin(@NotNull String oriStr, @NotNull String prefix) {
        if (prefix.equals(EMPTY)) {
            return oriStr;
        }
        int index;
        while ((index = oriStr.indexOf(prefix)) > 0) {
            oriStr = oriStr.substring(index);
        }
        return oriStr;
    }

    /**
     * 使用连接字符串连接map
     * @param oriMap 原始map
     * @param connectBetweenKeyAndValue 键值间连接字符串
     * @param connectBetweenPairs 键值对间连接字符串
     * @param <K> map键类型
     * @param <V> map值类型
     * @return 拼接后的字符串
     */
    public static <K, V> String joinMapWithConnectSymbols(@NotNull Map<K, V> oriMap, @NotNull String connectBetweenKeyAndValue, @NotNull String connectBetweenPairs) {
        StringBuilder strBud = new StringBuilder();
        for (Map.Entry<K, V> entry : oriMap.entrySet()) {
            strBud.append(entry.getKey()).append(connectBetweenKeyAndValue).append(entry.getValue()).append(connectBetweenPairs);
        }
        return trimEnd(strBud.toString(), connectBetweenPairs);
    }

    /**
     * 将字符串按分隔字符串拆分成map
     * @param oriStr 原始字符串
     * @param splitBetweenKeyAndValue 键值间拆分字符串
     * @param splitBetweenPairs 键值对间拆分字符串
     * @return map 返回Map
     */
    public static Map<String, String> splitStringInToMap(@NotNull String oriStr, @NotNull String splitBetweenKeyAndValue, @NotNull String splitBetweenPairs) {
        Map<String, String> retMap = new HashMap<String, String>(10);
        String[] pairs = oriStr.split(splitBetweenPairs);
        for (String keyAndValue : pairs) {
            int index = keyAndValue.indexOf(splitBetweenKeyAndValue);
            String key = keyAndValue.substring(0, index);
            String value = keyAndValue.substring(index + 1);
            retMap.put(key, value);
        }
        return retMap;
    }
}
