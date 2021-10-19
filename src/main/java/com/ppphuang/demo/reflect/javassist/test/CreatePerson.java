package com.ppphuang.demo.reflect.javassist.test;

import javassist.*;

import java.lang.reflect.Method;

public class CreatePerson {
    public static void createPerson() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //1. 创建一个空类
        CtClass cc = pool.makeClass("com.ppphuang.demo.reflect.javassist.test.Person");

        //2. 新增一个字段 private String name;
        CtField name = new CtField(pool.get("java.lang.String"), "name", cc);
        //设置访问级别
        name.setModifiers(Modifier.PRIVATE);
        // 设置初始值为xiaoming
        cc.addField(name, CtField.Initializer.constant("xiaoming"));
        //3. 生成getter setter方法
        cc.addMethod(CtNewMethod.setter("setName", name));
        cc.addMethod(CtNewMethod.getter("getName", name));
        //4. 添加无参构造方法
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, cc);
        ctConstructor.setBody("{name = \"xiaohong\";}");
        cc.addConstructor(ctConstructor);
        //5. 添加有参构造方法
        ctConstructor = new CtConstructor(new CtClass[]{pool.get("java.lang.String")}, cc);
        // $0=this, $1 $2 $3... 代表方法参数
        ctConstructor.setBody("{$0.name = $1;}");
        cc.addConstructor(ctConstructor);
        //6. 创建名为printName方法，无参数，无返回值，输出name值
        CtMethod printName = new CtMethod(CtClass.voidType, "printName", new CtClass[]{}, cc);
        printName.setModifiers(Modifier.PUBLIC);
        printName.setBody("{System.out.println(name);}");
        cc.addMethod(printName);
        //一. 输出Class文件
        cc.writeFile("/Users/ppphuang/Documents/code/java/src/main/java/");
        //二. 实例化生成的类
        Object person = cc.toClass().newInstance();
        //设置值
        Method setName = person.getClass().getMethod("setName", String.class);
        setName.invoke(person, "pengpeng");
        //输出值
        Method sout = person.getClass().getMethod("printName");
        sout.invoke(person);

    }

    public static void main(String[] args) {
        try {
//            createPerson();
            getFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFromFile() {
        ClassPool pool = ClassPool.getDefault();
        try {
            //三. 通过生成的Class文件调用
            pool.appendClassPath("/Users/ppphuang/Documents/code/java/src/main/java");
            CtClass ctClass = pool.get("com.ppphuang.demo.reflect.javassist.test.Person");
            Object person = ctClass.toClass().newInstance();
            //设置值
            Method setName = person.getClass().getMethod("setName", String.class);
            setName.invoke(person, "nbb");
            //输出值
            Method sout = person.getClass().getMethod("printName");
            sout.invoke(person);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
