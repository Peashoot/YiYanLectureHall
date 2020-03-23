package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.VisitRecordDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRecordMapper {
    VisitRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId);

    int insert(VisitRecordDO insertItem);

    int remove(Long removeId);

    int removeRange(List<Long> removeIdList);

    List<VisitRecordDO> selectAll();

    VisitRecordDO selectById(Long id);
}
