package com.ppphuang.demo.reflect.javassist.serverproxy;


public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name, Integer age) {
        return name + age;
    }
}¬
