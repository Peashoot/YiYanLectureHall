package com.peashoot.blog.batis.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import com.peashoot.blog.util.json.deserializer.EnumCodec;

@JSONType(serializer = EnumCodec.class, deserializer = EnumCodec.class)
public enum PermissionEnum implements BaseEnum<PermissionEnum, String> {
    /**
     * 用户登录
     */
    USER_LOGIN("login"),
    /**
     * 用户注册
     */
    USER_REGISTER("register"),
    /**
     * 登出
     */
    USER_LOGOUT("logout"),
    /**
     * 改密码
     */
    USER_CHANGE_PWD("change_pwd"),
    /**
     * 改其他信息
     */
    USER_CHANGE_DETAIL("change_detail"),
    /**
     * 申请重置密码
     */
    USER_APPLY_RESET_PWD("apply_reset_pwd"),
    /**
     * 申请找回账号
     */
    USER_APPLY_RETRIEVE_ACCOUNT("apply_retrieve"),
    /**
     * 重置密码
     */
    USER_RESET_PWD("reset_pwd"),
    /**
     * 账号
     */
    USER_RETRIEVE_ACCOUNT("retrieve")
    ;

    private PermissionEnum(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public String getValue() {
        return value;
    }
}
