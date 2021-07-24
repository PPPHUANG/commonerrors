package com.ppphuang.demo.jmm;

/**
 * 懒惰实例化
 * 首次使用 getInstance() 才使用 synchronized 加锁，后续使用时无需加锁
 * 有隐含的，但很关键的一点：第一个 if 使用了 INSTANCE 变量，是在同步块之外
 */
public final class DoubleCheckedLocking {
    private DoubleCheckedLocking() { }
    private static volatile DoubleCheckedLocking INSTANCE = null;
    public static DoubleCheckedLocking getInstance() {
        // 实例没创建，才会进入内部的 synchronized代码块
        if (INSTANCE == null) {
            synchronized (DoubleCheckedLocking.class) {
                // 也许有其它线程已经创建实例，所以再判断一次
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckedLocking();
                }
            }
        }
        return INSTANCE;
    }
}