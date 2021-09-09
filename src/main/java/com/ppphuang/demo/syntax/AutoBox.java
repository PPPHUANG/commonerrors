package com.ppphuang.demo.syntax;

public class AutoBox {
    /**
     * 包装类的 '==' 运算在不遇到算是运算的情况下不会自动拆箱，它们的equals()方法不处理数据转型的关系
     *
     * @param args
     */
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 4;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        //false
        System.out.println(c == d);
        //false
        System.out.println(e == f);
        //true
        System.out.println(c == (a + b));
        //true
        System.out.println(c.equals(a + b));
        //true
        System.out.println(g == (a + b));
        //false
        System.out.println(g.equals(a + b));
    }
}
