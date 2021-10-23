package com.ppphuang.demo.reflect.javassist.process;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class TestTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("Transforming " + className);
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get("com.ppphuang.demo.reflect.javassist.process.Base");
            CtMethod m = cc.getDeclaredMethod("process");
            m.insertBefore("{ System.out.println(\"start\"); }");
            m.insertAfter("{ System.out.println(\"end\"); }");
            byte[] bytes = cc.toBytecode();
            if (cc.isFrozen()) {
                cc.defrost();
            }
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
