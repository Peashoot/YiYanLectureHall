package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.mapper.OperateRecordMapper;
import com.peashoot.blog.batis.service.OperateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class OperateRecordServiceImpl implements OperateRecordService {
    private final OperateRecordMapper visitRecordMapper;

    public OperateRecordServiceImpl(OperateRecordMapper visitRecordMapper) {
        this.visitRecordMapper = visitRecordMapper;
    }

    @Override
    public OperateRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId) {
        return visitRecordMapper.selectLastRecordByVisitorIdAndCommentId(visitorId, commentId);
    }

    @Override
    public OperateRecordDO selectLastRecordByVisitorIdAndArticleId(long visitorId, String articleId) {
        return visitRecordMapper.selectLastRecordByVisitorIdAndArticleId(visitorId, articleId);
    }

    @Async
    @Override
    public Future<Boolean> insertNewRecordAsync(long visitorId, String objectId, VisitActionEnum action, Date operateDate, String record) {
        OperateRecordDO visitRecordDO = new OperateRecordDO();
        visitRecordDO.setVisitorId(visitorId);
        visitRecordDO.setAction(action);
        visitRecordDO.setActionDate(operateDate);
        visitRecordDO.setOperateObjectId(objectId);
        visitRecordDO.setRecord(record);
        return new AsyncResult<>(visitRecordMapper.insert(visitRecordDO) > 0);
    }

    @Override
    public int insert(OperateRecordDO insertItem) {
        return visitRecordMapper.insert(insertItem);
    }

    @Override
    public int remove(Long removeId) {
        return visitRecordMapper.remove(removeId);
    }

    @Override
    public int removeRange(List<Long> removeIdList) {
        return visitRecordMapper.removeRange(removeIdList);
    }

    @Override
    public List<OperateRecordDO> selectAll() {
        return visitRecordMapper.selectAll();
    }

    @Override
    public OperateRecordDO selectById(Long id) {
        return visitRecordMapper.selectById(id);
    }
}
