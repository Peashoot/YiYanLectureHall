package com.peashoot.blog.batis.service.base;

import com.peashoot.blog.batis.entity.base.BaseEntity;

import java.util.List;

public interface SelectService<T1 extends BaseEntity<T2>, T2> {
    /**
     * 获取所有的查询结果
     * @return 获取所有的查询结果
     */
    List<T1> selectAll();

    /**
     * 根据id查询
     * @param id 主键id
     * @return 查询结果
     */
    T1 selectById(T2 id);
}
