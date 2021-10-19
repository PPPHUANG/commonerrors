package com.ppphuang.demo.reflect.javassist.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

import java.lang.reflect.Method;

/**
 * 修改已存在的JAVA文件
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
