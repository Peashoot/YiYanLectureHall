package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateRecordMapper {
    OperateRecordDO selectLastRecordByVisitorIdAndObjectId(@Param("visitorId") Long visitorId, @Param("objectId") String objectId, @Param("actions")VisitActionEnum[] actions);

    int insert(OperateRecordDO insertItem);

    List<OperateRecordDO> selectAll();

    OperateRecordDO selectByPrimaryKey(Long id);
}
