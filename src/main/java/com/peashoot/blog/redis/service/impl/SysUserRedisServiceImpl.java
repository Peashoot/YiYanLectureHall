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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
     * Redis 存储键值间分隔符
     */
    private final String REDIS_VALUE_KEY_VALUE_DELIMITER = ":";
    /**
     * Redis 存储键值对间分隔符
     */
    private final String REDIS_VALUE_PAIRS_DELIMITER = ";";
    /**
     * 用户Token存储Redis token字段名
     */
    private final String USER_TOKEN_REDIS_VALUE_TOKEN = "token";
    /**
     * 用户Token存储Redis 超时时间字段名
     */
    private final String USER_TOKEN_REDIS_VALUE_EXPIRE_TIME = "expireTime";
    /**
     * 用户申请单号存储Redis 编号字段名
     */
    private final String APPLY_REDIS_VALUE_SERIAL = "applySerial";
    /**
     * 用户申请单号存储Redis 幂等字段名
     */
    private final String APPLY_REDIS_VALUE_IDEMPOTENT = "idempotent";
    /**
     * 用户申请单号存储Redis 超期时间字段名
     */
    private final String APPLY_REDIS_VALUE_EXPIRE_TIME = "expireTime";

    public SysUserRedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void recordGenerateToken(String username, String token, long expireTime) {
        Map<String, Object> userTokenMap = new TreeMap<>();
        userTokenMap.put(USER_TOKEN_REDIS_VALUE_TOKEN, token);
        userTokenMap.put(USER_TOKEN_REDIS_VALUE_EXPIRE_TIME, expireTime);
        saveMapInfoIntoRedis(userTokenMap, USER_LOGIN_TOKEN_HASH_REDIS_KEY, username);
    }

    /**
     * 将Map信息保存到Redis中
     * @param map map
     * @param redisKey redis键
     * @param hashKey hash键
     */
    private void saveMapInfoIntoRedis(Map<String, Object> map, String redisKey, String hashKey) {
        String storeMsgInfo = StringUtils.joinMapWithConnectSymbols(map, REDIS_VALUE_KEY_VALUE_DELIMITER, REDIS_VALUE_PAIRS_DELIMITER);
        try {
            String afterEncodingByBase64 = new String(Base64.encodeBase64(storeMsgInfo.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
            redisTemplate.opsForHash().put(redisKey, hashKey, afterEncodingByBase64);
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
            Map<String, String> userTokenMap = StringUtils.splitStringInToMap(afterDecodeFromBase64, REDIS_VALUE_KEY_VALUE_DELIMITER, REDIS_VALUE_PAIRS_DELIMITER);
            String oldToken = userTokenMap.get(USER_TOKEN_REDIS_VALUE_TOKEN);
            long oldExpireTime = Long.valueOf(userTokenMap.get(USER_TOKEN_REDIS_VALUE_EXPIRE_TIME));
            // 当前时间 大于 过期时间 或 token不等于oldToken 时认为用户需要重新登录
            return System.currentTimeMillis() > oldExpireTime || !oldToken.equals(token);
        } catch (Exception e) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e);
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
                Map<String, String> appInfoMap = StringUtils.splitStringInToMap(afterDecodeFromBase64, REDIS_VALUE_KEY_VALUE_DELIMITER, REDIS_VALUE_PAIRS_DELIMITER);
                applySerial = appInfoMap.get(APPLY_REDIS_VALUE_SERIAL);
                long idempotentTime = Long.valueOf(appInfoMap.get(APPLY_REDIS_VALUE_IDEMPOTENT));
                // 当前时间 大于 幂等时间 需要重新生成申请单号
                if (System.currentTimeMillis() > idempotentTime) {
                    applySerial = createApplyInfo(username, idempotentInterval, expiration);
                }
            } catch (Exception e) {
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
        Map<String, Object> applyInfoMap = new TreeMap<>();
        applyInfoMap.put(APPLY_REDIS_VALUE_SERIAL, applySerial);
        applyInfoMap.put(APPLY_REDIS_VALUE_IDEMPOTENT, System.currentTimeMillis() + idempotentInterval);
        applyInfoMap.put(APPLY_REDIS_VALUE_EXPIRE_TIME, System.currentTimeMillis() + expiration);
        saveMapInfoIntoRedis(applyInfoMap, USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username);
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
            Map<String, String> appInfoMap = StringUtils.splitStringInToMap(afterDecodeFromBase64, REDIS_VALUE_KEY_VALUE_DELIMITER, REDIS_VALUE_PAIRS_DELIMITER);
            String oldApplySerial = appInfoMap.get(APPLY_REDIS_VALUE_SERIAL);
            long expireTime = Long.valueOf(appInfoMap.get(APPLY_REDIS_VALUE_EXPIRE_TIME));
            if (System.currentTimeMillis() <= expireTime && oldApplySerial.equals(applySerial)) {
                redisTemplate.opsForHash().delete(USER_RESET_PWD_SERIAL_HASH_REDIS_KEY, username);
                return true;
            }
        } catch (Exception e) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }
        return false;
    }
}
