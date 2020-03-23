package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.VisitRecordDO;
import com.peashoot.blog.batis.mapper.VisitRecordMapper;
import com.peashoot.blog.batis.service.VisitRecordService;
import com.peashoot.blog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VisitRecordServiceImpl implements VisitRecordService {
    @Autowired
    private VisitRecordMapper visitRecordMapper;

    @Override
    public VisitRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId) {
        return visitRecordMapper.selectLastRecordByVisitorIdAndCommentId(visitorId, commentId);
    }

    @Override
    public boolean insertNewRecord(long visitorId, int objectId, VisitActionEnum action, Date operateDate, String record) {
        VisitRecordDO visitRecordDO = new VisitRecordDO();
        visitRecordDO.setVisitorId(visitorId);
        visitRecordDO.setAction(action);
        visitRecordDO.setActionDate(operateDate);
        visitRecordDO.setOperateObjectId(objectId);
        visitRecordDO.setRecord(record);
        return visitRecordMapper.insert(visitRecordDO) > 0;
    }

    @Override
    public int insert(VisitRecordDO insertItem) {
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
    public List<VisitRecordDO> selectAll() {
        return visitRecordMapper.selectAll();
    }

    @Override
    public VisitRecordDO selectById(Long id) {
        return visitRecordMapper.selectById(id);
    }
}
