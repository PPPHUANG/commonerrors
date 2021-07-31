package com.ppphuang.demo.syntax;

/**
 * 一维数组深拷贝  二维数组浅拷贝 只拷贝引用地址
 */
public class ArrayClone {
    public static void main(String[] args) {
        String[] a = {"1", "2"};
        String[] aa = a.clone();
        aa[0] = "22";
        System.out.println(a[0]);

        String[][] e = {{"0", "1"}, {"r", "b"}};
        String[][] ee = e.clone();
        ee[0][0] = "22";
        System.out.println(e[0][0]);
    }
}
