package com.peashoot.blog.context.response.article;

import com.peashoot.blog.batis.entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleIntroduction {
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
    private String keyword1;
    /**
     * 关键词2
     */
    private String keyword2;
    /**
     * 关键词3
     */
    private String keyword3;
    /**
     * 关键词4
     */
    private String keyword4;
    /**
     * 关键词5
     */
    private String keyword5;
    /**
     * 文件URL链接
     */
    private String articleURL;
    /**
     * 数据库实体转成返回实体
     */
    public static ArticleIntroduction createArticlesInfo(Article articleEntity) {
        ArticleIntroduction articleInfo = new ArticleIntroduction();
        articleInfo.title = articleEntity.getTitle();
        articleInfo.keyword1 = articleEntity.getKeyword1();
        articleInfo.keyword2 = articleEntity.getKeyword2();
        articleInfo.keyword3 = articleEntity.getKeyword3();
        articleInfo.keyword4 =  articleEntity.getKeyword4();
        articleInfo.keyword5 = articleEntity.getKeyword5();
        articleInfo.overview = articleEntity.getOverview();
        articleInfo.pageview = articleEntity.getPageView();
        articleInfo.articleURL = articleEntity.getArticleURL();
        return articleInfo;
    }
}
