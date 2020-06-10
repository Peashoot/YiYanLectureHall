package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = false)
public class ExceptionRecordDO extends LongPrimaryEntity {
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数名: 参数值（用;隔开，原来的;进行转义\;）
     */
    private String paramValues;
    /**
     * 异常错误
     */
    private String exceptionMsg;
    /**
     * 发生的时间
     */
    private Date appearTime;
}
