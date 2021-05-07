package com.ppphuang.demo.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Child2 extends Parent<String> {
    @Override
    public void setValue(String value) {
        System.out.println("Child1.setValue called");
        super.setValue(value);
    }

    public static void main(String[] args) {
        Child2 child1 = new Child2();
        Method[] declaredMethods = child1.getClass().getDeclaredMethods();
//        getMethods 和 getDeclaredMethods 是有区别的，前者可以查询到父类方法，后者只能查询到当前类。
//        反射进行方法调用要注意过滤桥接方法。
        Arrays.stream(declaredMethods).filter(method -> method.getName().equals("setValue") && !method.isBridge()).findFirst().ifPresent(method -> {
            try {
                method.invoke(child1,"test");
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(child1.toString());
    }
}
