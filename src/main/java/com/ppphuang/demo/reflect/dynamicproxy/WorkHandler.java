package com.ppphuang.demo.reflect.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class WorkHandler implements InvocationHandler {
    private Object obj;

    public WorkHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before 动态代理...");
        System.out.println(proxy.getClass().getName());
        System.out.println(this.obj.getClass().getName());
        if ("work".equals(method.getName())) {
            method.invoke(this.obj, args);
            System.out.println("after 动态代理...");
            //proxy返回真实代理类 后续可以继续使用也有增强功能
            return proxy;
            //this返回WorkHandler类
            //return this;
            //this.obj返回被代理类 后续调用没有代理增强
            //return this.obj;
        } else {
            System.out.println("after 动态代理...");
            return method.invoke(this.obj, args);
        }
    }
}
