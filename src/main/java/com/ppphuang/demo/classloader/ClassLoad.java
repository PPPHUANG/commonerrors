package com.ppphuang.demo.classloader;

import sun.misc.Launcher;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoad {
    public static void main(String[] args) {
        //核心rt.jar中的类加载器 c++加载的 这里获取为null
        System.out.println(String.class.getClassLoader());
        //扩展包的加载器 ExtClassLoader
        System.out.println(com.sun.crypto.provider.DESedeKeyFactory.class.getClassLoader());
        //应用加载器 APPClassLoader
        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println("");
        //获取系统ClassLoader
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        //APPClassLoader的父加载器
        ClassLoader extClassLoader = appClassLoader.getParent();
        //extClassLoader的父加载器
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println("the bootstrapClassLoader: " + bootstrapClassLoader);
        System.out.println("the extClassLoader: " + extClassLoader);
        System.out.println("the appClassLoader: " + appClassLoader);

        System.out.println("=============bootstrapClassLoader加载的文件============");
        URL[] urls= Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url);
        }
        System.out.println("");

        System.out.println("=============extClassLoader加载的文件============");
        URLClassLoader extClassLoader1 = (URLClassLoader) extClassLoader;
        for (URL url : extClassLoader1.getURLs()) {
            System.out.println(url);
        }
        System.out.println("");

        System.out.println("=============appClassLoader加载的文件============");
        URLClassLoader extClassLoader2 = (URLClassLoader) appClassLoader;
        for (URL url : extClassLoader2.getURLs()) {
            System.out.println(url);
        }
    }
}
