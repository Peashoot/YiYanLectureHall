package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class OperateRecordDO extends LongPrimaryEntity {
    /**
     * 访客id
     */
    private Long operatorId;
    /**
     * 访客行为
     */
    private VisitActionEnum action;
    /**
     * 访客操作对象
     */
    private String operateObjectId;
    /**
     * 具体访问记录
     */
    private String record;
    /**
     * 操作时间
     */
    private Date actionDate;
    /**
     * 操作状态 0 失败 1 成功
     */
    private Boolean actionResult;
    /**
     * 操作员类型 0 访客 1 系统操作员
     */
    private Integer operatorType;
}
