package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VisitRecordDO extends LongPrimaryEntity {
    /**
     * 访客id
     */
    private Long visitorId;
    /**
     * 访客行为
     */
    private VisitActionEnum action;
    /**
     * 访客操作对象
     */
    private Integer operateObjectId;
    /**
     * 具体访问记录
     */
    private String record;
    /**
     * 操作时间
     */
    private Date actionDate;
}
