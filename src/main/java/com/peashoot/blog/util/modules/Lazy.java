package com.peashoot.blog.util.modules;

public class Lazy<T> {
    /**
     * 储存实体
     */
    private volatile T lazyInstance;
    /**
     * 是否已经初始化过
     */
    private boolean hasInit;
    /**
     * 初始化对象
     */
    private final ILazyInitialize<T> initialize;
    public Lazy(ILazyInitialize<T> initializer) {
        this.initialize = initializer;
    }

    /**
     * 获取初始化后实体
     * @return 实体
     */
    public T getInstance(){
        synchronized (Lazy.class) {
            if (hasInit) {
                return lazyInstance;
            }
            synchronized (Lazy.class) {
                lazyInstance = initialize.lazyInit();
                hasInit = true;
                return lazyInstance;
            }
        }
    }
}
