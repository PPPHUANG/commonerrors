package com.ppphuang.demo.reflect.javassist.test;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * 上面两种其实都是通过反射的方式去调用，问题在于我们的工程中其实并没有这个类对象，
 * 所以反射的方式比较麻烦，并且开销也很大。那么如果你的类对象可以抽象为一些方法得合集，
 * 就可以考虑为该类生成一个接口类。这样在newInstance()的时候我们就可以强转为接口，可以将反射的那一套省略掉了。
 */
public class PrisonImpl {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath("/Users/ppphuang/Documents/code/java/src/main/java");
        // 获取接口
        CtClass ctClassI = pool.get("com.ppphuang.demo.reflect.javassist.test.PersonI");
        // 获取生成类
        CtClass ctClass = pool.get("com.ppphuang.demo.reflect.javassist.test.Person");
        //使生成的类实现PersonI接口
        ctClass.setInterfaces(new CtClass[]{ctClassI});
        //以下通过接口直接调用 强转
        PersonI person = (PersonI) ctClass.toClass().newInstance();
        System.out.println(person.getName());
        person.setName("nihao");
        person.printName();
    }
}
