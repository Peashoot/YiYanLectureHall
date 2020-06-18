package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class CommentDO extends LongPrimaryEntity {
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
    private Boolean anonymous;
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
    private Long commentTo;
    /**
     * 访客名称
     */
    private String visitorName;
    /**
     * 系统用户昵称
     */
    private String sysUserNickname;
    /**
     * 评论主体
     */
    private CommentDO commentToObject;

    public CommentDO() {
        supportCount = 0;
        status = 1;
        againstCount = 0;
    }
}