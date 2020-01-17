package com.peashoot.blog.service.base;

import com.peashoot.blog.entity.base.BaseEntity;

public interface InsertService<T extends BaseEntity> {
    /**
     * 新增至数据库
     * @param insertItem 新增对象
     * @return 执行结果
     */
    int insert(T insertItem);
}
