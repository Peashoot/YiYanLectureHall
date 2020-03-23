package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDO extends IntPrimaryEntity {
    /**
     * 评论人昵称
     */
    private Long visitorId;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 文件id
     */
    private String articleId;
    /**
     * 评论时间
     */
    private Date commentTime;
    /**
     * 显示状态 0: 待审核; 1:显示中; 2: 已删除
     */
    private Integer status;
    /**
     * 是否匿名
     */
    private boolean anonymous;
    /**
     * 支持统计
     */
    private Integer supportCount;
    /**
     * 反对统计
     */
    private Integer againstCount;
    /**
     * 指向其他评论
     */
    private Integer commentTo;
}