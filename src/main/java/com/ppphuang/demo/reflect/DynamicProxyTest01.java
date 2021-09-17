package com.ppphuang.demo.reflect;

import java.lang.reflect.*;

public class DynamicProxyTest01 {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> proxyClass = Proxy.getProxyClass(Hello.class.getClassLoader(), Hello.class);
        InvocationHandler handler = (proxy, method, args1) -> {
            System.out.println("proxy invoked");
            return method.invoke(new HelloImpl(), args1);
        };
        Hello hello = (Hello) proxyClass.getConstructor(InvocationHandler.class).newInstance(handler);
        String s = hello.sayHi();
        System.out.println(s);
    }
}

class HelloImpl implements Hello {
    @Override
    public String sayHi() {
        return "hello world";
    }
}

interface Hello {
    String sayHi();
}
