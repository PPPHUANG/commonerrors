package com.ppphuang.demo.jmm.singleton;

public final class SingletonE {
    private SingletonE() { }

    /**
     *   问题1：属于懒汉式还是饿汉式：懒汉式，这是一个静态内部类。类加载本身就是懒惰的，在没有调用getInstance方法时是没有执行LazyHolder内部类的类加载操作的。
     */
    private static class LazyHolder {
        static final SingletonE INSTANCE = new SingletonE();
    }

    /**
     *   问题2：在创建时是否有并发问题，这是线程安全的，类加载时，jvm保证类加载操作的线程安全
     */
    public static SingletonE getInstance() {
        return LazyHolder.INSTANCE;
    }
}