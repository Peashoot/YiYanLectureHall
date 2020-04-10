package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.mapper.OperateRecordMapper;
import com.peashoot.blog.batis.service.OperateRecordService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class OperateRecordServiceImpl implements OperateRecordService {
    private final OperateRecordMapper operateRecordMapper;

    public OperateRecordServiceImpl(OperateRecordMapper visitRecordMapper) {
        this.operateRecordMapper = visitRecordMapper;
    }

    @Override
    public OperateRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId) {
        return operateRecordMapper.selectLastRecordByVisitorIdAndObjectId(visitorId, String.valueOf(commentId), new VisitActionEnum[]{
                VisitActionEnum.AGREE_COMMENT, VisitActionEnum.DISAGREE_COMMENT, VisitActionEnum.CANCEL_AGREE_COMMENT, VisitActionEnum.CANCEL_DISAGREE_COMMENT});
    }

    @Override
    public OperateRecordDO selectLastRecordByVisitorIdAndArticleId(long visitorId, String articleId) {
        return operateRecordMapper.selectLastRecordByVisitorIdAndObjectId(visitorId, articleId, new VisitActionEnum[]{
                VisitActionEnum.AGREE_ARTICLE, VisitActionEnum.DISAGREE_ARTICLE, VisitActionEnum.CANCEL_AGREE_ARTICLE, VisitActionEnum.CANCEL_DISAGREE_ARTICLE});
    }

    @Async
    @Override
    public Future<Boolean> insertNewRecordAsync(long visitorId, String objectId, VisitActionEnum action, Date operateDate, String record) {
        OperateRecordDO visitRecordDO = new OperateRecordDO();
        visitRecordDO.setOperatorId(visitorId);
        visitRecordDO.setAction(action);
        visitRecordDO.setActionDate(operateDate);
        visitRecordDO.setOperateObjectId(objectId);
        visitRecordDO.setRecord(record);
        return new AsyncResult<>(operateRecordMapper.insert(visitRecordDO) > 0);
    }

    @Override
    public int insert(OperateRecordDO insertItem) {
        return operateRecordMapper.insert(insertItem);
    }

    @Override
    public List<OperateRecordDO> selectAll() {
        return operateRecordMapper.selectAll();
    }

    @Override
    public OperateRecordDO selectById(Long id) {
        return operateRecordMapper.selectByPrimaryKey(id);
    }
}
