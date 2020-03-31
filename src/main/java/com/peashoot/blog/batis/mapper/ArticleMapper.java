package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.ArticleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int deleteRangeByPrimaryKeys(@Param("articleIds")Integer[] articleIds);

    int insert(ArticleDO record);

    int insertSelective(ArticleDO record);

    ArticleDO selectByPrimaryKey(String id);

    List<ArticleDO> selectAll();

    int updateByPrimaryKeySelective(ArticleDO record);

    int updateByPrimaryKey(ArticleDO record);

    List<ArticleDO> listPagedArticles(int indexStart, int selectCount, String authorLike, String keywordLike, String titleLike);

    int countTotalRecords(String authorLike, String keywordLike, String titleLike);

    ArticleDO selectByPrimaryKeyForUpdate(String articleId);
}