package com.peashoot.blog.aspect.annotation;

import java.lang.annotation.*;

/**
 * 错误日志记录注解
 *
 * @author peashoot
 */
@Target({ElementType.TYPE, ElementType.METHOD})//作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Documented//表明这个注解应该被 javadoc工具记录
public @interface ErrorRecord {
}
