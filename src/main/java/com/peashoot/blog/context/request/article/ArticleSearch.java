package com.peashoot.blog.context.request.article;

import com.peashoot.blog.context.request.ApiReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleSearch extends ApiReq {
    /**
     * 单页可容纳最大数据条数
     */
    private int pageSize;
    /**
     * 当前页数
     */
    private int pageIndex;
    /**
     * 根据关键字查询
     */
    private String keywordLike;
    /**
     * 根据标题查询
     */
    private String titleLike;
    /**
     * 根据作者查询
     */
    private String authorLike;
}
