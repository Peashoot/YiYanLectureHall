package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.CommentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int deleteRangeByPrimaryKeys(@Param("commentIds") Long[] commentIds);

    int insert(CommentDO record);

    int insertSelective(CommentDO record);

    CommentDO selectByPrimaryKey(Long id);

    CommentDO selectByPrimaryKeyForUpdate(Long id);

    List<CommentDO> selectAll();

    int updateByPrimaryKeySelective(CommentDO record);

    int updateByPrimaryKey(CommentDO record);

    List<CommentDO> listPagedComments(@Param("selectCount") int pageSize, @Param("indexStart") int pageIndex, @Param("articleId") String articleId);

    int countTotalRecords(String articleId);
}