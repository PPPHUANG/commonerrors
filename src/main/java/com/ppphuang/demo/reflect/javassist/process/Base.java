package com.ppphuang.demo.reflect.javassist.process;

import java.lang.management.ManagementFactory;

/**
 * 待增强的类
 */
public class Base {
    public static void main(String[] args) {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String s = name.split("@")[0];
        // 打印当前 Pid
        System.out.println("pid:" + s);
        while (true) {
            try {
                Thread.sleep(5000L);
            } catch (Exception e) {
                break;
            }
            process();
        }
    }
    public static void process() {
        System.out.println("process");
    }
}
