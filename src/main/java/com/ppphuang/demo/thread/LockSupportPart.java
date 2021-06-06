package com.ppphuang.demo.thread;

import java.util.concurrent.locks.LockSupport;

public class LockSupportPart {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("park...");
            LockSupport.park();
            System.out.println("unpark...");
            //Thread.currentThread().isInterrupted() 获取了打断标志之后不会重置
//            System.out.println("打断状态..." + Thread.currentThread().isInterrupted());
            //Thread.interrupted() 获取了打断状态之后会重置为false
            System.out.println("打断状态..." + Thread.interrupted());
            //打断标志为false的时候才会生效
            LockSupport.park();
            System.out.println("unpark...");

        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
