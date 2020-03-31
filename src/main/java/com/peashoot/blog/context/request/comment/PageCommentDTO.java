package com.peashoot.blog.context.request.comment;

import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = false)
public class PageCommentDTO extends BaseApiReq {
    /**
     * 单页最大容量
     */
    private Integer pageSize;
    /**
     * 页码
     */
    private Integer pageIndex;
    /**
     * 文章id
     */
    private String articleId;
}
