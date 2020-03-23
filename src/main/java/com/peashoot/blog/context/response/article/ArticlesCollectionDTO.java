package com.peashoot.blog.context.response.article;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticlesCollectionDTO {
    /**
     * 文章信息列表
     */
    private List<ArticleIntroductionDTO> articleList;
    /**
     * 单页最大数据条数
     */
    private int pageSize;
    /**
     * 分页当前页数
     */
    private int pageIndex;
    /**
     * 满足条件的所有记录数
     */
    private int totalRecordsCount;
    /**
     * 分页查找出的当前页满足条件的记录数
     */
    private int matchRecordsCount;
}

