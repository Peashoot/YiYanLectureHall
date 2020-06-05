package com.peashoot.blog.context.request.comment;

import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class CommentAgreeDTO extends BaseApiReq {
    /**
     * 访客ID
     */
    private Long visitorId;
    /**
     * 评论ID
     */
    private Integer commentId;
    /**
     * 是否同意
     */
    private VisitActionEnum action;
}
