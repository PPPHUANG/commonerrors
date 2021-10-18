package com.ppphuang.demo.charles;

import javassist.*;

import java.io.IOException;

/**
 * 本代码仅供学习交流，请支持正版
 * Charles破解jar
 */
public class Main1 {

    /**
     * 实例化类型池
     */
    public static ClassPool pool = ClassPool.getDefault();

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        // 获取默认类型池对象
        pool.insertClassPath("/Applications/Charles.app/Contents/Java/charles.jar");
        // 从类型池中读取指定类型
        CtClass oFTR = pool.get("com.xk72.charles.p");
        try {
            // 取得需要修改的方法
            CtMethod ctMethod = oFTR.getDeclaredMethod("a", null);
            // 修改方法体直接return true;
            ctMethod.setBody("{return true;}");
            ctMethod = oFTR.getDeclaredMethod("c", null);
            ctMethod.setBody("{return \"Regisered PPPHUANG\";}");
            //将上面构造好的类写入到指定的工作空间中
            oFTR.writeFile("/Users/ppphuang/Documents/code/java/src/main/java/com/ppphuang/demo/charles/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
