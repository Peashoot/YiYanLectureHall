package com.peashoot.blog.util.modules;

public interface ILazyInitialize<T> {
    /**
     * 懒加载初始化对象委托
     * @return
     */
     T lazyInit();
}
