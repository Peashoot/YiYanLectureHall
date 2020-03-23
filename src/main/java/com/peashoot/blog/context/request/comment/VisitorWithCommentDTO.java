package com.peashoot.blog.context.request.comment;

import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VisitorWithCommentDTO extends BaseApiReq {
    /**
     * 用户名
     */
    private Long visitorId;
    /**
     * 是否匿名
     */
    private boolean anonymous;
    /**
     * 文章id
     */
    private String articleId;
    /**
     * 评论
     */
    private String comment;
    /**
     * 评论的内容
     */
    private Integer commentTo;
    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 创建评论实体
     * @return 评论数据库实体
     */
    public CommentDO createCommentEntity() {
        CommentDO comment = new CommentDO();
        comment.setVisitorId(visitorId);
        comment.setAnonymous(anonymous);
        comment.setArticleId(articleId);
        comment.setComment(this.comment);
        comment.setCommentTime(commentTime);
        comment.setCommentTo(commentTo);
        return comment;
    }
}
