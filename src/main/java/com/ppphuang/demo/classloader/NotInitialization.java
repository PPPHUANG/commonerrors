package com.ppphuang.demo.classloader;

public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
//        SuperClass
//        123
//        对于静态字段，只有直接定义这个字段的类才会被初始化，
//        因此通过其子类来引用父类中定义的静态字段，
//        只会触发父类的初始化而不会触发子类的初始化。
//        至于是否要触发子类的加载和验证阶段，
//        在《Java虚拟机规范》中并未明确规定，所以这点取决于虚拟机的具体实现。

        SuperClass[] superClasses = new SuperClass[10];
        //不会输出SuperClass
    }
}
