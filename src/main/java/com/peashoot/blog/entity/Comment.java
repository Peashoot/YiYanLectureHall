package com.peashoot.blog.entity;

import com.peashoot.blog.entity.base.IntPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Comment extends IntPrimaryEntity {
    /**
     * 评论人昵称
     */
    private String nickName;
    /**
     * 系统用户id
     */
    private Integer userId;
    /**
     * 系统用户
     */
    private SysUser user;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 文件id
     */
    private Integer articleId;
    /**
     * 文件
     */
    private Article article;
    /**
     * 评论时间
     */
    private Date commentTime;
    /**
     * 显示状态 0: 待审核; 1:显示中; 2: 已删除
     */
    private Integer status;
}