package com.peashoot.blog.batis.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import com.peashoot.blog.util.json.deserializer.EnumCodec;

@JSONType(serializer = EnumCodec.class, deserializer = EnumCodec.class)
public enum GenderEnum implements BaseEnum<GenderEnum, Integer> {
    /**
     * 未知
     */
    Unknown(0),
    /**
     * 男
     */
    Man(1),
    /**
     * 女
     */
    Woman(2);

    private GenderEnum(int value) {
        this.value = value;
    }

    private int value;

    @Override
    public Integer getValue() {
        return value;
    }
}
