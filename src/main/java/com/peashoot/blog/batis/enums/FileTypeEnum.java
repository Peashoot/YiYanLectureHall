package com.peashoot.blog.batis.enums;

import com.peashoot.blog.batis.enums.base.BaseEnum;

public enum FileTypeEnum implements BaseEnum<FileTypeEnum, String> {
    /**
     * 图片
     */
    PICTURE("pic"),
    /**
     * 文本文档
     */
    TEXT_FILE("txt"),
    /**
     * md文档
     */
    MARKDOWN("md") {};

    String value;

    FileTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
