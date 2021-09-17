package com.ppphuang.demo.reflect.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EnhancerTest {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "hello world " + args[0];
                } else {
                    return methodProxy.invokeSuper(obj, args);
                }
            }
        });
        PersonService personService = (PersonService) enhancer.create();
        String s = personService.sayHi("666");
        System.out.println(s);
        int tom = personService.lengthOfName("tom");
        System.out.println(tom);
    }
}

class PersonService {
    public String sayHi(String name) {
        return "hello " + name;
    }

    public int lengthOfName(String name) {
        return name.length();
    }
}