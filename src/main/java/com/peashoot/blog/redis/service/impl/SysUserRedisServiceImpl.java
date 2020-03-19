package com.peashoot.blog.redis.service.impl;

import com.peashoot.blog.redis.service.SysUserRedisService;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@Service
public class SysUserRedisServiceImpl implements SysUserRedisService {
    /**
     * Redis存储
     */
    @Autowired
    StringRedisTemplate redisTemplate;
    /**
     * 用户登录token存储的Hash名称
     */
    final String USERLOGINTOKEN = "user_login_token";
    /**
     * 用户重置密码申请编号的Hash名称
     */
    final String USERRESETPWDSERIAL = "user_reset_pwd_apply_serial";
    /**
     * 默认的编码方式
     */
    final String DEFAULTENCODING = "UTF-8";

    @Override
    public void recordGenerateToken(String username, String token, long expireTime) {
        String storeMsgInfo = "token:" + token + ";expireTime:" + expireTime;
        try {
            String afterEncodingByBase64 = new String(Base64.encodeBase64(storeMsgInfo.getBytes(DEFAULTENCODING)), DEFAULTENCODING);
            redisTemplate.opsForHash().put(USERLOGINTOKEN, username, afterEncodingByBase64);
        } catch (UnsupportedEncodingException ex) {
            log.throwing(getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
        }
    }

    @Override
    public boolean checkIfNeedReLogin(String username, String token) {
        Object recordInfo = redisTemplate.opsForHash().get(USERLOGINTOKEN, username);
        if (recordInfo == null) {
            return true;
        }
        String recordMsgInfo = recordInfo.toString();
        try {
            String afterDecodeFromBase64 = new String(Base64.decodeBase64(recordMsgInfo.getBytes(DEFAULTENCODING)), DEFAULTENCODING);
            String regex = "^token:(.+);expireTime:(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(afterDecodeFromBase64);
            if (matcher.find()) {
                String oldToken = matcher.group(1);
                long oldExpireTime = Long.valueOf(matcher.group(2));
                // 当前时间 大于 过期时间 或 token不等于oldToken 时认为用户需要重新登录
                return System.currentTimeMillis() > oldExpireTime || !oldToken.equals(oldToken);
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
        if (!redisTemplate.opsForHash().hasKey(USERLOGINTOKEN, username)) {
            return true;
        }
        return redisTemplate.opsForHash().delete(USERLOGINTOKEN, username) > 0;
    }

    @Override
    public void saveResetPwdApplySerial(String username, String applySerial) {
        redisTemplate.opsForHash().put(USERRESETPWDSERIAL, username, applySerial);
    }

    @Override
    public boolean checkIfMatchResetPwdApplySerial(String username, String applySerial) {
        if (redisTemplate.opsForHash().hasKey(USERRESETPWDSERIAL, username)) {
            return false;
        }
        if (applySerial == redisTemplate.opsForHash().get(USERRESETPWDSERIAL, username)) {
            redisTemplate.opsForHash().delete(USERRESETPWDSERIAL, username);
            return true;
        }
        return false;
    }
}
