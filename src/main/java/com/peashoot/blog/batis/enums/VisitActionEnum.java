package com.peashoot.blog.batis.enums;

import com.peashoot.blog.batis.enums.base.BaseEnum;

public enum VisitActionEnum implements BaseEnum<VisitActionEnum, Integer> {
    /**
     * 新增文章
     */
    CREATE_ARTICLE(80),
    /**
     * 更新文章
     */
    UPDATE_ARTICLE(90),
    /**
     * 删除文章
     */
    DELETE_ARTICLE(99),
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
    CHANGE_NAME(301),
    /**
     * 创建角色
     */
    ROLE_CREATE(401),
    /**
     * 更新角色
     */
    ROLE_UPDATE(402),
    /**
     * 删除角色
     */
    ROLE_REMOVE(403),
    /**
     * 上传文件
     */
    UPLOAD_FILE(501),
    /**
     * 生成访客信息
     */
    VISITOR_GENERATE(601),
    /**
     * 用户登录
     */
    USER_LOGIN(701),
    /**
     * 用户注册
     */
    USER_REGISTER(702),
    /**
     * 用户登出
     */
    USER_LOGOUT(703),
    /**
     * 用户修改密码
     */
    USER_CHANGE_PASSWORD(704),
    /**
     * 用户申请找回密码
     */
    USER_APPLY_RETRIEVE_PASSWORD(705),
    /**
     * 用户找回密码
     */
    USER_RETRIEVE_PASSWORD(706),
    /**
     * 用户修改信息
     */
    USER_CHANGE_INFORMATION(707){
    };

    private int value;

    VisitActionEnum(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
