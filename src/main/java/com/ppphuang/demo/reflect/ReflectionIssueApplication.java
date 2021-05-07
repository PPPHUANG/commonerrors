package com.ppphuang.demo.reflect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionIssueApplication {
    private void age(int age) {
        log.info("int age = {}", age);
    }
    private void age(Integer age) {
        log.info("Integer age = {}", age);
    }

    public static void main(String[] args) throws Exception {
        ReflectionIssueApplication reflectionIssueApplication = new ReflectionIssueApplication();
//        对象调用方法是以传参决定重载
//        reflectionIssueApplication.age(36);
//        reflectionIssueApplication.age(Integer.valueOf(36));
//        反射调用方法不是以传参决定重载，而是通过方法签名来确定方法
        reflectionIssueApplication.right();
        reflectionIssueApplication.wrong();
    }

    public void wrong() throws Exception {
//        Integer.TYPE 代表的是 int
        getClass().getDeclaredMethod("age",Integer.TYPE).invoke(this,Integer.valueOf("36"));
    }

    public void right() throws Exception {
//        Integer.TYPE 改为 Integer.class，执行的参数类型就是包装类型的 Integer
        getClass().getDeclaredMethod("age",Integer.class).invoke(this, Integer.valueOf("36"));
        getClass().getDeclaredMethod("age",Integer.class).invoke(this, 36);
    }
}
