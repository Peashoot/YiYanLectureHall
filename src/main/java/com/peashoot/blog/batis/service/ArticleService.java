package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.ArticleDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

import java.util.List;

public interface ArticleService extends InsertService<ArticleDO>, UpdateService<ArticleDO>,
        SelectService<ArticleDO, String>, RemoveService<ArticleDO, String> {
    /**
     * 分页获取文章
     *
     * @param pageSize    单页最大记录数
     * @param pageIndex   当前页数
     * @param authorLike  部分作者名
     * @param keywordLike 关键字部分
     * @param titleLike   部分标题
     * @return 满足条件的结果
     */
    List<ArticleDO> listPagedArticles(int pageSize, int pageIndex, String authorLike, String keywordLike, String titleLike);

    /**
     * 获取满足条件的记录总条数
     *
     * @param authorLike  部分作者名
     * @param keywordLike 部分关键字
     * @param titleLike   部分标题
     * @return 满足条件的结果总数
     */
    int countTotalRecords(String authorLike, String keywordLike, String titleLike);

    /**
     * 更新文章的赞成和反对数
     *
     * @param articleId 文章id
     * @param agree     赞成数增量
     * @param disagree  反对数增量
     * @return 是否更新成功
     */
    boolean updateSupportAndDisagreeState(String articleId, int agree, int disagree);
}
