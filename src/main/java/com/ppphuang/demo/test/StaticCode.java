package com.ppphuang.demo.test;

public class StaticCode {
    static class StaticCodeA{
        StaticCodeA() {
            System.out.println("This is a parent construct block");
        }
        static {
            System.out.println("This is a parent static block");
        }
        {
            System.out.println("This is a parent block");
        }
    }
    static class ParentStaticCode extends StaticCodeA {
        ParentStaticCode() {
            System.out.println("This is a  construct block");
        }
        static {
            System.out.println("This is a  static block");
        }
        {
            System.out.println("This is a  block");
        }
    }

//    区别很简单:
//    静态代码块，在虚拟机加载类的时候就会加载执行，而且只执行一次;
//    非静态代码块，在创建对象的时候(即new一个对象的时候)执行，每次创建对象都会执行一次
//    一个程序可以有多个静态非静态代码区域。

//    This is a parent static block
//    This is a  static block
//    This is a parent block
//    This is a parent construct block
//    This is a  block
//    This is a  construct block

    public static void main(String[] args) {
        new ParentStaticCode();
    }
}
