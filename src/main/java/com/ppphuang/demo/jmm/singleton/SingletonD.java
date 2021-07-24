package com.ppphuang.demo.jmm.singleton;

public final class SingletonD {
    private SingletonD() { }
    // 问题1：解释为什么要加 volatile ?为了防止重排序问题,导致第一个INSTANCE != null时拿到一个未执行构造方法的对象
    private static volatile SingletonD INSTANCE = null;

    // 问题2：对比实现3, 说出这样做的意义：提高了效率
    public static SingletonD getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SingletonD.class) {
            // 问题3：为什么还要在这里加为空判断, 之前不是判断过了吗？这是为了第一次判断时的并发问题。
            if (INSTANCE != null) {
                return INSTANCE;
            }
            // 可能指令重排 先赋值 后执行构造方法
            INSTANCE = new SingletonD();
            return INSTANCE;
        }
    }
}