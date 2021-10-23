package com.ppphuang.demo.reflect.javassist.preagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import lombok.SneakyThrows;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentMain {
    public static void premain(String arg, Instrumentation instrumentation) {
        final ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        instrumentation.addTransformer(new ClassFileTransformer() {
            @SneakyThrows
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"com/ppphuang/demo/reflect/javassist/preagent/AgentTest".equals(className)) {
                    return null;
                }
                CtClass ctClass = pool.get("com.ppphuang.demo.reflect.javassist.preagent.AgentTest");
                CtMethod sayHi = ctClass.getDeclaredMethod("sayHi");
//                sayHi.insertBefore(" long begin = System.currentTimeMillis();");
                //下面的语句会报找不到begin变量，因为javassist插入的代码都是代码块，下一个代码块获取不到上一个代码块的局部变量
//                sayHi.insertAfter(" System.out.println(System.currentTimeMillis()-begin);");
                CtMethod copy = CtNewMethod.copy(sayHi, ctClass, null);
                copy.setName("sayHi$agent");
                ctClass.addMethod(copy);
                sayHi.setBody("{long begin = System.currentTimeMillis(); sayHi$agent($1);System.out.println(System.currentTimeMillis()-begin);}");
                return ctClass.toBytecode();
            }
        });
    }
}
