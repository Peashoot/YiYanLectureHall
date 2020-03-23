package com.peashoot.blog.batis.entity;

public enum VisitActionEnum {
    /**
     * 点赞文章
     */
    AGREE_ARTICLE(101),
    /**
     * 反对文章
     */
    DISAGREE_ARTICLE(102),
    /**
     * 取消点赞文章
     */
    CANCEL_AGREE_ARTICLE(199),
    /**
     * 取消反对文章
     */
    CANCEL_DISAGREE_ARTICLE(198),
    /**
     * 评论
     */
    COMMENT(200),
    /**
     * 点赞评论
     */
    AGREE_COMMENT(201),
    /**
     * 反对评论
     */
    DISAGREE_COMMENT(202),
    /**
     * 取消点赞评论
     */
    CANCEL_AGREE_COMMENT(299),
    /**
     * 取消反对评论
     */
    CANCEL_DISAGREE_COMMENT(298),
    /**
     * 绑定系统用户
     */
    BIND_SYS_USER(300),
    /**
     * 修改名称
     */
    CHANGE_NAME(301) {
    };

    private int value;

    VisitActionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
