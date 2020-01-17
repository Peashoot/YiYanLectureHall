package com.peashoot.blog.mapper.base;

import com.peashoot.blog.entity.base.BaseEntity;

public interface UpdateMapper<T extends BaseEntity> {
    /**
     * 更新对象
     * @param updateItem 要更新的对象
     * @return 执行结果
     */
    int update(T updateItem);
}
