package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.OperateRecordDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateRecordMapper {
    OperateRecordDO selectLastRecordByVisitorIdAndCommentId(long visitorId, int commentId);

    OperateRecordDO selectLastRecordByVisitorIdAndArticleId(long visitorId, String articleId);

    int insert(OperateRecordDO insertItem);

    int remove(Long removeId);

    int removeRange(List<Long> removeIdList);

    List<OperateRecordDO> selectAll();

    OperateRecordDO selectById(Long id);
}
