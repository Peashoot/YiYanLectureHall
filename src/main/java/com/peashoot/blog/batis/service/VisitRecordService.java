package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.VisitRecordDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;

import java.util.Date;

public interface VisitRecordService extends InsertService<VisitRecordDO>,
        SelectService<VisitRecordDO, Long>, RemoveService<VisitRecordDO, Long> {
    /**
     * 根据访客id和评论id获取最后一条记录
     * @param visitorId 访客id
     * @param commentId 评论id
     * @return 符合条件的记录
     */
    VisitRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId);

    /**
     * 新增一条记录
     * @param visitorId 访客id
     * @param objectId 操作对象id
     * @param action 操作类型
     * @param operateDate 操作时间
     * @param record 操作具体内容
     * @return 是否新增成功
     */
    boolean insertNewRecord(long visitorId, int objectId, VisitActionEnum action, Date operateDate, String record);
}
