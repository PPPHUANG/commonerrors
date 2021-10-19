package com.ppphuang.demo.reflect.javassist.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

import java.lang.reflect.Method;

/**
 * 修改已存在的JAVA文件
 * insertBefore() 和 setBody()中的语句，如果你是单行语句可以直接用双引号，
 * 但是有多行语句的情况下，你需要将多行语句用{}括起来。javassist只接受单个语句或用大括号括起来的语句块。
 */
public class UpdatePersonService {
    public static void update() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.ppphuang.demo.reflect.javassist.test.PersonService");

        //增加环绕增强
        CtMethod personFly = ctClass.getDeclaredMethod("personFly");
        personFly.insertBefore("System.out.println(\"before personFly\");");
        personFly.insertAfter("System.out.println(\"after personFly\");");
        //增加新方法
        CtMethod joinFriend = new CtMethod(CtClass.voidType, "joinFriend", new CtClass[]{}, ctClass);
        joinFriend.setModifiers(Modifier.PUBLIC);
        joinFriend.setBody("{System.out.println(\"i want you\");}");
        ctClass.addMethod(joinFriend);

        Object personService = ctClass.toClass().newInstance();
        Method personFly1 = personService.getClass().getMethod("personFly");
        personFly1.invoke(personService);
        Method joinFriend1 = personService.getClass().getMethod("joinFriend");
        joinFriend1.invoke(personService);
    }

    public static void main(String[] args) {
        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
