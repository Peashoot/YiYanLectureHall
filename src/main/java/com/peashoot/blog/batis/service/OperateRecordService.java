package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;

import java.util.Date;
import java.util.concurrent.Future;

public interface OperateRecordService extends InsertService<OperateRecordDO>,
        SelectService<OperateRecordDO, Long>{
    /**
     * 根据访客id和评论id获取最后一条记录
     * @param visitorId 访客id
     * @param commentId 评论id
     * @return 符合条件的记录
     */
    OperateRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId);

    /**
     * 根据访客id和文章id获取最后一条记录
     * @param visitorId 访客id
     * @param articleId 文章id
     * @return 符合条件的记录
     */
    OperateRecordDO selectLastRecordByVisitorIdAndArticleId(long visitorId, String articleId);

    /**
     * 异步新增一条记录
     * @param visitorId 访客id
     * @param objectId 操作对象id
     * @param visitIp 操作ip
     * @param action 操作类型
     * @param operateDate 操作时间
     * @param record 操作具体内容
     * @return 是否新增成功
     */
    Future<Boolean> insertNewRecordAsync(long visitorId, String objectId, String visitIp, VisitActionEnum action, Date operateDate, String record);
}
