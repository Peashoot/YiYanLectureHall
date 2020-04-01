package com.peashoot.blog.aspect.annotation;

import com.peashoot.blog.util.Constant;

import java.lang.annotation.*;

/**
 * 限制访客在单位时间内的访问次数
 * @author peashoot
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Documented//表明这个注解应该被 javadoc工具记录
public @interface VisitLimit {
    /**
     * 最大访问次数
     */
    public int value() default 10;

    /**
     * 统计时间区间
     */
    public int interval() default 60 * Constant.MILLISECONDS_PEY_SECOND;

    /**
     * 到期清零
     */
    public boolean maturityClear() default true;
}
