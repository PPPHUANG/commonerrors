package com.ppphuang.demo.reflect.javassist;

import java.lang.reflect.Method;

public interface InvocationHandler {
    /**
     * @param proxy  动态生成的代理对象
     * @param method 调用的方法
     * @param args   调用的参数
     * @return 该方法的返回值
     * @throws Throwable
     */
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
