package com.ppphuang.demo.classloader;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass");
    }
}
