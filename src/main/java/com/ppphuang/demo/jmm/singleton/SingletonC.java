package com.ppphuang.demo.jmm.singleton;

public final class SingletonC {
    private SingletonC() { }
    private static SingletonC INSTANCE = null;
    // 分析这里的线程安全, 并说明有什么缺点：synchronized加载静态方法上，可以保证线程安全。缺点就是锁的范围过大
    public static synchronized SingletonC getInstance() {
        if( INSTANCE != null ){
            return INSTANCE;
        }
        INSTANCE = new SingletonC();
        return INSTANCE;
    }
}
