package com.peashoot.blog.redis.service.impl;

import com.peashoot.blog.aspect.annotation.VisitLimit;
import com.peashoot.blog.redis.service.VisitLimitRedisService;
import com.peashoot.blog.util.EncryptUtils;
import com.peashoot.blog.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public boolean isAllowVisit(String visitIp, String browserFingerprint, Date visitTime, VisitLimit visitLimit) {
        if (visitLimit.maturityClear()) {
            return isLimitedVisitMaturityClear(visitIp, browserFingerprint, visitTime, visitLimit);
        } else {
            return isLimitedVisitNotMaturityClear(visitIp, browserFingerprint, visitTime, visitLimit);
        }
    }

    /**
     * 区间起始值和区间统计值正则表达式
     */
    private final Pattern spanStartAndSpanCountPattern = Pattern.compile("^limitStart=(\\d+)&limitCount=(\\d+)$");

    /**
     * 是否允许访问（区间自动清零）
     *
     * @param visitIp            访问IP
     * @param browserFingerprint 浏览器指纹
     * @param visitTime          访问时间
     * @param visitLimit         注解
     * @return 是否允许
     */
    private boolean isLimitedVisitMaturityClear(String visitIp, String browserFingerprint, Date visitTime, VisitLimit visitLimit) {
        String needMaturityClearKey = "visit_limit_count_need_clear";
        String generateHashKey = EncryptUtils.md5Encrypt(visitIp + browserFingerprint);
        String limitCountString = redisTemplate.<String, String>opsForHash().get(needMaturityClearKey, generateHashKey);
        boolean result = false;
        Date lastVisitTime = visitTime;
        int visitCount = 1;
        if (StringUtils.isNullOrEmpty(limitCountString)) {
            result = true;
        } else {
            try {
                Matcher matcher = spanStartAndSpanCountPattern.matcher(limitCountString);
                lastVisitTime = new Date(Long.valueOf(matcher.group(1)));
                visitCount = Integer.valueOf(matcher.group(2));
                // 如果上次统计时间超过了统计区间，刷新统计时间及访问次数
                if (lastVisitTime.getTime() + visitLimit.interval() > visitTime.getTime()) {
                    lastVisitTime = visitTime;
                    visitCount = 1;
                    result = true;
                } else if (visitCount < visitLimit.value()) {
                    visitCount++;
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        limitCountString = "limitStart=" + lastVisitTime.getTime() + "&limitCount=" + visitCount;
        redisTemplate.<String, String>opsForHash().put(needMaturityClearKey, generateHashKey, limitCountString);
        return result;
    }

    /**
     * 是否允许访问（区间累计）
     *
     * @param visitIp            访问IP
     * @param browserFingerprint 浏览器指针
     * @param visitTime          访问时间
     * @param visitLimit         注解
     * @return 是否允许
     */
    private boolean isLimitedVisitNotMaturityClear(String visitIp, String browserFingerprint, Date visitTime, VisitLimit visitLimit) {
        String notMaturityClearKey = "visit_limit_count_not_clear";
        String generateHashKey = EncryptUtils.md5Encrypt(visitIp + browserFingerprint);
        List<Date> visitHistory = redisTemplate.<String, List<Date>>opsForHash().get(notMaturityClearKey, generateHashKey);
        boolean result = false;
        if (visitHistory == null) {
            visitHistory = new ArrayList<Date>() {{
                add(visitTime);
            }};
            result = true;
        } else {
            // 移除不在统计时间区间内的访问时间
            visitHistory.removeAll(visitHistory.stream().filter(i -> i.getTime() + visitLimit.interval() > visitTime.getTime()).collect(Collectors.toList()));
            // 如果统计时间段内访问次数达到限制
            if (visitHistory.size() < visitLimit.value()) {
                visitHistory.add(visitTime);
                result = true;
            }
        }
        redisTemplate.<String, List<Date>>opsForHash().put(notMaturityClearKey, generateHashKey, visitHistory);
        return result;
    }
}
