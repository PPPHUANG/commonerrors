package com.ppphuang.demo.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class DynamicProxyTest02 {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //我们可能会碰到，在不同的接口中存在同名方法的情况，此时invoke()方法调用哪个代理接口的方法会按照传递给代理类的代理接口的顺序，即getProxyClass()方法内的接口的顺序，而不是实现类继承接口的顺序
        Class<?> proxyClass = Proxy.getProxyClass(Hello.class.getClassLoader(), Hello01.class, Hello02.class);
//        Class<?> proxyClass = Proxy.getProxyClass(Hello.class.getClassLoader(), Hello02.class, Hello01.class);
        InvocationHandler handler = (proxy, method, args1) -> {
            System.out.println(method.getDeclaringClass().getName());
            return method.invoke(new HelloImpl2(), args1);
        };
        Hello02 hello = (Hello02) proxyClass.getConstructor(InvocationHandler.class).newInstance(handler);
        String s = hello.sayHi();
        System.out.println(s);
    }
}

class HelloImpl2 implements Hello01, Hello02 {
    @Override
    public String sayHi() {
        return "hello world";
    }
}

interface Hello01 {
    String sayHi();
}

interface Hello02 {
    String sayHi();
}