package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.CommentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteRangeByPrimaryKeys(@Param("commentIds") Integer[] commentIds);

    int insert(CommentDO record);

    int insertSelective(CommentDO record);

    CommentDO selectByPrimaryKey(Integer id);

    CommentDO selectByPrimaryKeyForUpdate(Integer id);

    List<CommentDO> selectAll();

    int updateByPrimaryKeySelective(CommentDO record);

    int updateByPrimaryKey(CommentDO record);

    List<CommentDO> listPagedComments(int pageSize, int pageIndex, String articleId);

    int countTotalRecords(String articleId);
}