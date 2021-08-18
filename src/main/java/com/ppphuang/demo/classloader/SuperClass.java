package com.ppphuang.demo.classloader;

public class SuperClass {
    public static int value = 123;
    static{
        System.out.println("SuperClass");
    }
}
