package com.peashoot.blog.context.request.comment;

import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class VisitorWithCommentDTO extends BaseApiReq {
    /**
     * 用户名
     */
    @NotNull
    @NotBlank
    private String visitor;
    /**
     * 是否匿名
     */
    @NotNull
    private Boolean anonymous;
    /**
     * 文章id
     */
    @NotNull
    private String articleId;
    /**
     * 评论
     */
    @NotNull
    @NotBlank
    private String comment;
    /**
     * 评论的内容
     */
    private Long commentTo;
    /**
     * 评论时间
     */
    @NotNull
    private Date commentTime;

    /**
     * 创建评论实体
     * @return 评论数据库实体
     */
    public CommentDO createCommentEntity(Long visitorId) {
        CommentDO comment = new CommentDO();
        comment.setVisitorId(visitorId);
        comment.setAnonymous(anonymous);
        comment.setArticleId(articleId);
        comment.setComment(this.comment);
        comment.setCommentTime(commentTime);
        comment.setCommentTo(Optional.ofNullable(commentTo).orElse(0L));
        return comment;
    }
}
