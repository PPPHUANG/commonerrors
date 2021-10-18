package com.ppphuang.demo.reflect.javassist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class proxyByJavassist {
    public static void main(String[] args) {
        try {
            Demo demo = new Demo();
            Class clazz = Demo.class;
            // 指定被代理对象的构造器，内部会自动转换为代理对象的构造器
            Constructor constructor = clazz.getConstructor(new Class[]{});
            Object[] constructorParam = new Object[]{};
            // 指定方法回调的接口
            InvocationHandler invocationHandler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("before:" + method.getName());
                    // 记住这儿是调用的被代理对象的方法，所以传参是 demo 而不是 proxy
                    method.setAccessible(true);
                    Object result = method.invoke(demo, args);
                    System.out.println("after:" + method.getName());
                    return result;
                }
            };
            Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), invocationHandler, clazz, constructor, constructorParam);
            // 分别测试 public、protected、default的方法
            ((Demo) proxy).publicDemo();
            ((Demo) proxy).protectDemo();
            ((Demo) proxy).defaultDemo();
            // 测试继承的public方法
            System.out.println(proxy.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
