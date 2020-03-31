package com.peashoot.blog.util;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisUtils {
    /**
     * 如果hash表中的值和原来的值不同，则进行修改
     *
     * @param stringRedisTemplate redis操作类
     * @param redisKey            redisHash名
     * @param hashKey             键
     * @param newHashValue        新值
     * @param <HK>                键类型
     * @param <HV>                值类型
     * @return 是否更新
     */
    public static <HK, HV> boolean updateIfValueNotEqual(StringRedisTemplate stringRedisTemplate, String redisKey, HK hashKey, HV newHashValue) {
        boolean result = false;
        HashOperations<String, HK, HV> hashOpt = stringRedisTemplate.opsForHash();
        try {
            stringRedisTemplate.multi();
            HV oldVal = hashOpt.get(redisKey, hashKey);
            if (oldVal == null && newHashValue == null) {
            } else if (oldVal != null && !oldVal.equals(newHashValue)) {
            } else {
                hashOpt.put(redisKey, hashKey, newHashValue);
                result = true;
            }
            stringRedisTemplate.exec();
        } catch (Exception ex) {
            stringRedisTemplate.discard();
            result = false;
        }
        return result;
    }
}
