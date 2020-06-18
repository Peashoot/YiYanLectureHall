package com.peashoot.blog.redis.service.impl;

import com.peashoot.blog.aspect.annotation.VisitTimesLimit;
import com.peashoot.blog.redis.service.VisitLimitRedisService;
import com.peashoot.blog.util.EncryptUtils;
import com.peashoot.blog.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class VisitLimitRedisServiceImpl implements VisitLimitRedisService {
    /**
     * Redis存储
     */
    private final StringRedisTemplate redisTemplate;

    public VisitLimitRedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowVisit(String className, String methodName, String visitIp, String browserFingerprint, Date visitTime, VisitTimesLimit visitTimesLimit) {
        if (visitTimesLimit.maturityClear()) {
            return isLimitedVisitMaturityClear(className, methodName, visitIp, browserFingerprint, visitTime, visitTimesLimit);
        } else {
            return isLimitedVisitNotMaturityClear(className, methodName, visitIp, browserFingerprint, visitTime, visitTimesLimit);
        }
    }

    /**
     * 是否允许访问（区间自动清零）
     *
     * @param className          类型名称
     * @param methodName         方法名称
     * @param visitIp            访问IP
     * @param browserFingerprint 浏览器指纹
     * @param visitTime          访问时间
     * @param visitTimesLimit    注解
     * @return 是否允许
     */
    private boolean isLimitedVisitMaturityClear(String className, String methodName, String visitIp, String browserFingerprint, Date visitTime, VisitTimesLimit visitTimesLimit) {
        String generateHashKey = EncryptUtils.md5Encrypt(visitIp + browserFingerprint + className + methodName);
        String needMaturityClearKey = "visit_limit_count_need_clear_";
        String redisKey = needMaturityClearKey + generateHashKey;
        String limitCountString = redisTemplate.opsForValue().get(redisKey);
        boolean result = false;
        int visitCount = 1;
        if (StringUtils.isNullOrEmpty(limitCountString)) {
            result = true;
        } else {
            try {
                visitCount = Integer.valueOf(limitCountString);
                if (visitCount < visitTimesLimit.value()) {
                    result = true;
                    visitCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result) {
            redisTemplate.<String, String>opsForValue().set(redisKey, String.valueOf(visitCount), visitTimesLimit.interval());
        }
        return result;
    }

    /**
     * 是否允许访问（区间累计）
     *
     * @param className          类型名称
     * @param methodName         方法名称
     * @param visitIp            访问IP
     * @param browserFingerprint 浏览器指针
     * @param visitTime          访问时间
     * @param visitTimesLimit    注解
     * @return 是否允许
     */
    private boolean isLimitedVisitNotMaturityClear(String className, String methodName, String visitIp, String browserFingerprint, Date visitTime, VisitTimesLimit visitTimesLimit) {
        String generateHashKey = EncryptUtils.md5Encrypt(visitIp + browserFingerprint + className + methodName);
        String notMaturityClearKey = "visit_limit_count_not_clear_";
        String redisKey = notMaturityClearKey + generateHashKey;
        String visitHistoryStr = redisTemplate.opsForValue().get(redisKey);
        boolean result = false;
        List<Long> visitHistory = new ArrayList<Long>() {{
            add(visitTime.getTime());
        }};
        if (StringUtils.isNullOrEmpty(visitHistoryStr)) {
            result = true;
        } else {
            try {
                String[] visitHistoryStrArray = visitHistoryStr.split(",");
                visitHistory = Arrays.stream(visitHistoryStrArray).map(Long::valueOf).collect(Collectors.toList());
                // 移除不在统计时间区间内的访问时间
                visitHistory.removeAll(visitHistory.stream()
                        .filter(i -> i + visitTimesLimit.interval() < visitTime.getTime())
                        .collect(Collectors.toList()));
                // 如果统计时间段内访问次数达到限制
                if (visitHistory.size() < visitTimesLimit.value()) {
                    visitHistory.add(visitTime.getTime());
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result) {
            visitHistoryStr = String.join(",", visitHistory.stream().map(Object::toString).toArray(String[]::new));
            redisTemplate.opsForValue().set(redisKey, visitHistoryStr, visitTimesLimit.interval());
        }
        return result;
    }
}
