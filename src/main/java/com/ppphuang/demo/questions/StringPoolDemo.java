package com.ppphuang.demo.questions;

public class StringPoolDemo {
    public static void main(String[] args) {
        String s = new StringBuilder("te").append("st").toString();
        System.out.println(s);
        System.out.println(s.intern());
        System.out.println(s.intern().equals(s));
        System.out.println(s.intern() == s);


        //这里openJDK运行是true是因为openJDK中的初始化不会初始化Java字符串 而是一些别的字符串 例如openjdk
        String s1 = new StringBuilder("ja").append("va").toString();
        System.out.println(s1);
        System.out.println(s1.intern());
        System.out.println(s1.intern().equals(s1));
        System.out.println(s1.intern() == s1);


        // sun.misc.Version;
        String s2 = new StringBuilder("open").append("jdk").toString();
        System.out.println(s2);
        System.out.println(s2.intern());
        System.out.println(s2.intern().equals(s2));
        System.out.println(s2.intern() == s2);

    }
}
