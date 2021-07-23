package com.ppphuang.demo.jmm;

/**
 * 可见性
 */
public class WhileTrue {
    static boolean run = true;
    static Object lock = new Object();
//    volatile static boolean run = true;
    public static void main(String[] args) {
        new Thread(()->{
            while (true) {
//                synchronized (lock) {
                    if (!run) {
                        break;
                    }
//                }
                //
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("停止线程");
        //线程不会按预想的停下了
        //当另一个线程中频繁调用主线程的变量时，JIT编译器会将主线程的变量缓存到自己所在的线程中，以提高访问效率
        //1. 当另一个线程中的while循环中有sleep时，读取不频繁，就不会缓存数据到自己的线程中
        //2. 当变量使用volatile修饰时也可不会缓存数据到自己的线程中
        //3. 当变量使用synchronized同步操作时也可不会缓存数据到自己的线程中
//        synchronized (lock) {
            run = false;
//        }
    }
}
