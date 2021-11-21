package com.ppphuang.demo.reflect.javassist.preagent;

public class AgentTest {
    public static void main(String[] args) {
        sayHi("hello world");
    }

    public static void sayHi(String name) {
        try {
            Thread.sleep(500);
            System.out.println(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sayHello(String name, Integer age) {
        System.out.println(name + age);
        return name + age;
    }

}
