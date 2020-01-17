package com.peashoot.blog.mapper;

import com.peashoot.blog.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Visitor record);

    int insertSelective(Visitor record);

    Visitor selectByPrimaryKey(Long id);

    List<Visitor> selectAll();

    int updateByPrimaryKeySelective(Visitor record);

    int updateByPrimaryKey(Visitor record);
}