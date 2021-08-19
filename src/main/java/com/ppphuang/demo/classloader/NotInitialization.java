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

        System.out.println(ConstClass.HELLOWORLD);
        //上述代码运行之后，也没有输出“ConstClass init！”，这是因为虽然在Java源码中确实引用了ConstClass类的常量HELLOWORLD，但其实在编译阶段通过常量传播优化，已经将此常量的值“hello world”直接存储在NotInitialization类的常量池中，以后NotInitialization对常量ConstClass.HELLOWORLD的引用，实际都被转化为NotInitialization类对自身常量池的引用了。
    }
}
