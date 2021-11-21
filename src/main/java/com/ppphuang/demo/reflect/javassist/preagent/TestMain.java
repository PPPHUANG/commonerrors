package com.ppphuang.demo.reflect.javassist.preagent;

import javassist.*;

public class TestMain {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
        final ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        //第一种方式
        CtClass ctClass = pool.get("com.ppphuang.demo.reflect.javassist.preagent.AgentTest");
        CtMethod sayHello = ctClass.getDeclaredMethod("sayHello");
        CtMethod copy = CtNewMethod.copy(sayHello, ctClass, null);
        int length = copy.getParameterTypes().length;
        System.out.println(length);
        copy.setName("sayHello$agent");
        ctClass.addMethod(copy);
        StringBuilder body = new StringBuilder("{return sayHello$agent(");
        for (int i = 1; i <= length; i++) {
            body.append("$").append(i);
            if (i != length) {
                body.append(",");
            }
        }
        body.append(");}");
        sayHello.setBody(body.toString());
        AgentTest pc = (AgentTest) ctClass.toClass().newInstance();
        String ppphuang = pc.sayHello("ppphuang", 18);
        System.out.println(ppphuang);

        //第二种方式
        CtClass agentTest$proxy = pool.makeClass("AgentTest$proxy");
        CtClass sayHello1 = pool.get("com.ppphuang.demo.reflect.javassist.preagent.SayHello");
        agentTest$proxy.addInterface(sayHello1);
        CtField proxyField = CtField.make("private static " +
                "com.ppphuang.demo.reflect.javassist.preagent.AgentTest" +
                " serviceProxy = new " +
                "com.ppphuang.demo.reflect.javassist.preagent.AgentTest" +
                "();", agentTest$proxy);
        //手动new 接口实现类 设置到属性
        agentTest$proxy.addField(proxyField);
        CtMethod make = CtMethod.make("public String sayHello(String name,Integer age){return serviceProxy.sayHello(name,age);}", agentTest$proxy);
        agentTest$proxy.addMethod(make);
        SayHello o = (SayHello) agentTest$proxy.toClass().newInstance();
        String ppphuang1 = o.sayHello("ppphuang", 18);
        System.out.println(ppphuang1);
    }
}
