package com.ppphuang.demo.synchronized1;


public class SyncPrint21 {
    private static Object lock = new Object();
    private static boolean runEd = false;
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                while (!runEd) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(1);
            }

        },"t1").start();
        new Thread(() -> {
            synchronized (lock) {
                System.out.println(2);
                runEd = true;
                lock.notify();
            }
        },"t2").start();

    }

}