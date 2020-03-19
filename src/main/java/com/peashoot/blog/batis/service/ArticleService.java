package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.Article;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

import java.util.List;

public interface ArticleService extends InsertService<Article>, UpdateService<Article>,
        SelectService<Article, Integer>, RemoveService<Article, Integer> {
    /**
     * 分页获取文章
     * @param pageSize 单页最大记录数
     * @param pageIndex 当前页数
     * @param authorLike 部分作者名
     * @param keywordLike 关键字部分
     * @param titleLike 部分标题
     * @return 满足条件的结果
     */
    List<Article> getArticlesByPage(int pageSize, int pageIndex, String authorLike, String keywordLike, String titleLike);

    /**
     * 获取满足条件的记录总条数
     * @param authorLike 部分作者名
     * @param keywordLike 部分关键字
     * @param titleLike 部分标题
     * @return 满足条件的结果总数
     */
    int getMatchedArticleTotalCount(String authorLike, String keywordLike, String titleLike);
}
