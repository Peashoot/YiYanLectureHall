package com.peashoot.blog.redis.service;

import com.peashoot.blog.aspect.annotation.VisitTimesLimit;

import java.util.Date;

/**
 * 访问统计Redis服务
 *
 * @author peashoot
 */
public interface VisitLimitRedisService {
    /**
     * 判断是否允许访问
     * @param visitIp 访问IP
     * @param browserFingerprint 浏览器指纹
     * @param visitTime 访问时间
     * @param visitTimesLimit 访问注解信息
     * @return 是否允许访问
     */
    boolean isAllowVisit(String visitIp, String browserFingerprint, Date visitTime, VisitTimesLimit visitTimesLimit);

}
