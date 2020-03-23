package com.peashoot.blog.context.response.comment;

import com.peashoot.blog.batis.entity.CommentDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleCommentDTO {
    /**
     * 评论id
     */
    private int id;
    /**
     * 评论日期
     */
    private Date commentTime;
    /**
     * 访客名称
     */
    private String visitor;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 评论主题（对哪条评论的回复）
     */
    private ArticleCommentDTO commentTo;
    /**
     * 赞成统计
     */
    private int supportCount;
    /**
     * 反对统计
     */
    private int againstCount;

    /**
     * 生成文章评论响应对象
     * @param commentEntity 评论数据库实体
     * @param commentTo 评论的评论
     * @param visitorName 访客名称
     * @return 响应对象
     */
    public static ArticleCommentDTO generateArticleComment(CommentDO commentEntity, ArticleCommentDTO commentTo, String visitorName) {
        ArticleCommentDTO articleComment = new ArticleCommentDTO();
        articleComment.comment = commentEntity.getComment();
        articleComment.commentTime = commentEntity.getCommentTime();
        articleComment.commentTo = commentTo;
        articleComment.id = commentEntity.getId();
        articleComment.visitor = visitorName;
        return articleComment;
    }
}
