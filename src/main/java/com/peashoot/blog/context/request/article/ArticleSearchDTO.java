package com.peashoot.blog.context.request.article;

import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ArticleSearchDTO extends BaseApiReq {
    /**
     * 单页可容纳最大数据条数
     */
    private Integer pageSize;
    /**
     * 当前页数
     */
    private Integer pageIndex;
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
