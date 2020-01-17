package com.peashoot.blog.util;

import org.jetbrains.annotations.NotNull;

public class StringUtils {
    /**
     * 将密码和盐通过隔位拼接的方式串在一起
     * password 密码
     * salt 盐
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
}
