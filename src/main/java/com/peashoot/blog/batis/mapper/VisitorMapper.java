package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.VisitorDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VisitorDO record);

    long insertWithReturnRecordID(VisitorDO record);

    int insertSelective(VisitorDO record);

    VisitorDO selectByPrimaryKey(Long id);

    List<VisitorDO> selectAll();

    int updateByPrimaryKeySelective(VisitorDO record);

    int updateByPrimaryKey(VisitorDO record);

    VisitorDO selectByIPAndBrowser(@Param("visitIP") String visitIP, @Param("browser") String browser);

    VisitorDO selectByVisitorName(String visitorName);
}