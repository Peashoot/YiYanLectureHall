package com.peashoot.blog.batis.enums;

import com.peashoot.blog.batis.enums.base.BaseEnum;

public enum OperatorTypeEnum implements BaseEnum<OperatorTypeEnum, Integer> {
    /**
     * 访客
     */
    VISITOR(0),
    /**
     * 管理员
     */
    ADMINISTRATOR(1);

    private OperatorTypeEnum(int value) {
        this.value = value;
    }

    private int value;
    @Override
    public Integer getValue() {
        return value;
    }
}
