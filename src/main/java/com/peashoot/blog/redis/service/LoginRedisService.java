package com.peashoot.blog.redis.service;

public interface LoginRedisService {
    boolean signalSignOnCheck(String username, String loginIP, String browserFingerprint);
}
