package com.ppphuang.demo.reflect.javassist.process;

import javassist.*;

import java.io.IOException;

public class JavassistTest {
    public static void main(String[] args) throws NotFoundException,
            CannotCompileException, IllegalAccessException, InstantiationException,
            IOException {
//        最后调用了 ClassLoader 的 native 方法
//        defineClass() 时报错。也就是说，JVM 是不允许在运行时动态重载一个类的。
//        Base b=new Base();
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.ppphuang.demo.reflect.javassist.process.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
//        cc.writeFile("/Users/ppphuang/Documents/code/java/src/main/java/com/ppphuang/demo/reflect/javassist/process/");
        Base h = (Base) c.newInstance();
        //h.process();
    }
}
