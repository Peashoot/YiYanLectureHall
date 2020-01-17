package com.peashoot.blog.mapper.base;

import com.peashoot.blog.entity.base.BaseEntity;

import java.util.List;

public interface RemoveMapper<T1 extends BaseEntity<T2>, T2> {
    /**
     * 根据主键id删除对象
     * @param removeId 主键id
     * @return 执行结果
     */
    int remove(T2 removeId);

    /**
     * 根据主键id批量删除
     * @param removeIdList 要删除的主键id列表
     * @return 执行结果
     */
    int removeRange(List<T2> removeIdList);
}
