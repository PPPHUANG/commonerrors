package com.ppphuang.demo.reflect;

import java.util.Arrays;

public class Child1 extends Parent {
    public void setValue(String value) {
        System.out.println("Child1.setValue called");
        super.setValue(value);
    }

    public static void main(String[] args) {
        Child1 child1 = new Child1();
        Arrays.stream(child1.getClass().getMethods()).filter(method -> method.getName().equals("setValue")).forEach(method -> {
            try {
                method.invoke(child1,"test");
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(child1.toString());
    }
}
