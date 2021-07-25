package com.ppphuang.demo.jmm;

import java.text.SimpleDateFormat;

public class DateTimeFormatterTest {
    /**
     * 思路 - 不可变对象
     * <p>
     * 如果一个对象在不能够修改其内部状态（属性），那么它就是线程安全的，因为不存在并发修改啊！这样的对象在
     * Java 中有很多，例如在 Java 8 后，提供了一个新的日期格式化类DateTimeFormatter：
     *
     * @param args
     */
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println(sdf.parse("1951-04-21"));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }).start();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println(sdf1.parse("1951-04-21"));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }).start();
        }
    }
}
