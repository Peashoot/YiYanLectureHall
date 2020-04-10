package com.peashoot.blog.context.response.article;

import com.peashoot.blog.batis.entity.ArticleDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ArticleIntroductionDTO {
    /**
     * 标题
     */
    private String title;
    /**
     * 文章概述
     */
    private String overview;
    /**
     * 浏览量
     */
    private long pageview;
    /**
     * 关键词1
     */
    private String keywords;
    /**
     * 文件URL链接
     */
    private String articleURL;
    /**
     * 数据库实体转成返回实体
     */
    public static ArticleIntroductionDTO createArticlesInfo(ArticleDO articleEntity) {
        ArticleIntroductionDTO articleInfo = new ArticleIntroductionDTO();
        articleInfo.title = articleEntity.getTitle();
        articleInfo.keywords = articleEntity.getKeywords();
        articleInfo.overview = articleEntity.getOverview();
        articleInfo.pageview = articleEntity.getPageView();
        articleInfo.articleURL = articleEntity.getArticleUrl();
        return articleInfo;
    }
}
