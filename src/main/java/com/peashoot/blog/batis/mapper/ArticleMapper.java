package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteRangeByPrimaryKeys(@Param("articleIds")Integer[] articleIds);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    List<Article> selectAll();

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> getArticlesByPage(int indexStart, int selectCount, String keywordLike, String titleLike);

    int getMatchedArticleTotalCount(String keywordLike, String titleLike);
}