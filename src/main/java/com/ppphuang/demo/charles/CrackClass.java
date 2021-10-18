package com.ppphuang.demo.charles;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class CrackClass {

    public static void crack(String oldJarPath, String outClassPath) throws NotFoundException, CannotCompileException, IOException {
        // 这个是得到反编译的池
        ClassPool pool = ClassPool.getDefault();

        // 取得需要反编译的jar文件，设定路径
        pool.insertClassPath(oldJarPath);

        // 取得需要反编译修改的文件，注意是完整路径
        CtClass cc1 = pool.get("com.xk72.charles.p");

        try {
            // 取得需要修改的方法
            CtMethod ctMethod = cc1.getDeclaredMethod("a", null);
            // 修改方法体直接return true;
            ctMethod.setBody("{return true;}");
            ctMethod = cc1.getDeclaredMethod("c", null);
            ctMethod.setBody("{return \"Regisered PPPHUANG\";}");

            cc1.writeFile(outClassPath);

            System.out.println("反编译class修改成功");
        } catch (NotFoundException e) {
            System.out.println("反编译class异常:" + e);
        }
    }
}
