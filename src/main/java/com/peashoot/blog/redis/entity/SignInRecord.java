package com.peashoot.blog.redis.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRecord {
    /**
     * 上一次登录时间
     */
    private long lastLoginTimestamp;
    /**
     * 浏览器指纹
     */
    private String browserFingerprint;
    /**
     * 上一次登录ip
     */
    private String lastLoginIP;
}
