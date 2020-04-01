package com.peashoot.blog.redis.service.impl;

import com.peashoot.blog.crypto.impl.Md5Crypto;
import com.peashoot.blog.redis.service.SysUserRedisService;
import com.peashoot.blog.util.EncryptUtils;
import com.peashoot.blog.util.StringUtils;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@Service
public class SysUserRedisServiceImpl implements SysUserRedisService {
    /**
     * Redis存储
     */
    private final StringRedisTemplate redisTemplate;
    /**
     * 用户登录token存储的Hash名称
     */
    private final String USER_LOGIN_TOKEN_HASH_REDIS_KEY = "user_login_token";
    /**
     * 用户重置密码申请编号的Hash名称
     */
    private final String USER_RESET_PWD_SERIAL_HASH_REDIS_KEY = "user_reset_pwd_apply_serial";
    /**
     * 默认的编码方式
     */
    private final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 用户登录redis存储hash值正则
     */
    private final Pattern USER_LOGIN_REDIS_HASH_VALUE_PATTERN = Pattern.compile("^token:(.+);expireTime:(.+)$");

    /**
     * 申请单号redis存储hash值正则
     */
    private final Pattern APPLY_SERIAL_REDIS_HASH_VALUE_PATTERN = Pattern.compile("^applySerial:(.+);idempotent:(.+);expireTime:(.+)$");

    public SysUserRedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void recordGenerateToken(String username, String token, long expireTime) {
        String storeMsgInfo = "token:" + token + ";expireTime:" + expireTime;
        try {
            String afterEncodingByBase64 = new String(Base64.encodeBase64(storeMsgInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
            redisTemplate.opsForHash().put(USER_LOGIN_TOKEN_HASH_REDIS_KEY, username, afterEncodingByBase64);
        } catch (UnsupportedEncodingException ex) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
        }
    }

    @Override
    public boolean checkIfNeedReLogin(String username, String token) {
        Object recordInfo = redisTemplate.opsForHash().get(USER_LOGIN_TOKEN_HASH_REDIS_KEY, username);
        if (recordInfo == null) {
            return true;
        }
        String recordMsgInfo = recordInfo.toString();
        try {
            String afterDecodeFromBase64 = new String(Base64.decodeBase64(recordMsgInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
            Matcher matcher = USER_LOGIN_REDIS_HASH_VALUE_PATTERN.matcher(afterDecodeFromBase64);
            if (matcher.find()) {
                String oldToken = matcher.group(1);
                long oldExpireTime = Long.valueOf(matcher.group(2));
                // 当前时间 大于 过期时间 或 token不等于oldToken 时认为用户需要重新登录
                return System.currentTimeMillis() > oldExpireTime || !oldToken.equals(token);
            } else {
                log.info(getClass().getName() + "->" + Thread.currentThread().getStackTrace()[1].getMethodName() + ":" + "redis登录存储信息解析失败");
                return true;
            }
        } catch (UnsupportedEncodingException ex) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
            return true;
        }
    }

    @Override
    public boolean removeUserTokenInfo(String username) {
        if (!redisTemplate.opsForHash().hasKey(USER_LOGIN_TOKEN_HASH_REDIS_KEY, username)) {
            return true;
        }
        return redisTemplate.opsForHash().delete(USER_LOGIN_TOKEN_HASH_REDIS_KEY, username) > 0;
    }

    @Override
    public String saveResetPwdApplySerial(String username, long idempotentInterval, long expiration) {
        String oldApplyInfo = redisTemplate.<String, String>opsForHash().get(USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username);
        String applySerial = StringUtils.EMPTY;
        if (StringUtils.isNullOrEmpty(oldApplyInfo)) {
            applySerial = createApplyInfo(username, idempotentInterval, expiration);
        } else {
            try {
                String afterDecodeFromBase64 = new String(Base64.decodeBase64(oldApplyInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
                Matcher matcher = APPLY_SERIAL_REDIS_HASH_VALUE_PATTERN.matcher(afterDecodeFromBase64);
                if (matcher.find()) {
                    applySerial = matcher.group(1);
                    long idempotentTime = Long.valueOf(matcher.group(2));
                    // 当前时间 大于 幂等时间 需要重新生成申请单号
                    if (System.currentTimeMillis() > idempotentTime) {
                        applySerial = createApplyInfo(username, idempotentInterval, expiration);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
        }
        return applySerial;
    }

    /**
     * 创建申请信息
     *
     * @param username           用户名
     * @param idempotentInterval 幂等时间
     * @param expiration         有效期
     * @return 返回申请单号
     */
    private String createApplyInfo(String username, long idempotentInterval, long expiration) {
        String applySerial = EncryptUtils.md5Encrypt(UUID.randomUUID().toString() + username);
        String applyInfo = "applySerial:" + applySerial +
                ";idempotent:" + (System.currentTimeMillis() + idempotentInterval) +
                ";expireTime:" + (System.currentTimeMillis() + expiration);
        try {
            String afterEncodingByBase64 = new String(Base64.encodeBase64(applyInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
            redisTemplate.opsForHash().put(USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username, afterEncodingByBase64);
        } catch (UnsupportedEncodingException ex) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
        }
        return applySerial;
    }


    @Override
    public boolean checkIfMatchResetPwdApplySerial(String username, String applySerial) {
        String oldApplyInfo = redisTemplate.<String, String>opsForHash().get(USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username);
        if (StringUtils.isNullOrEmpty(oldApplyInfo)) {
            return false;
        }
        try {
            String afterDecodeFromBase64 = new String(Base64.decodeBase64(oldApplyInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
            Matcher matcher = APPLY_SERIAL_REDIS_HASH_VALUE_PATTERN.matcher(afterDecodeFromBase64);
            if (matcher.find()) {
                String oldApplySerial = matcher.group(1);
                long expireTime = Long.valueOf(matcher.group(3));
                if (System.currentTimeMillis() <= expireTime && oldApplySerial.equals(applySerial)) {
                    redisTemplate.opsForHash().delete(USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username);
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }
        return false;
    }
}
