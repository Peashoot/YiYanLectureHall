package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteRangeByPrimaryKeys(@Param("commentIds") Integer[] commentIds);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    List<Comment> selectAll();

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
}