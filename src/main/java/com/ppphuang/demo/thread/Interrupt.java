package com.ppphuang.demo.thread;

public class Interrupt {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("我被打断了");
                    //如果不break的话 会一直循环打印下去
                    break;
                }
            }
        },"t1");
        t1.start();
        System.out.println("t1.interrupt");
        t1.interrupt();
    }
}
