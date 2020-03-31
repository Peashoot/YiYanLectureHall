package com.peashoot.blog.batis.entity;

public enum FileTypeEnum {
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

    public String getValue() {
        return value;
    }
}
