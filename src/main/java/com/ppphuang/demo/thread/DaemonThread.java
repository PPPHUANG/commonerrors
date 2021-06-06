package com.ppphuang.demo.thread;

import java.util.concurrent.TimeUnit;

public class DaemonThread {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("Thread ...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //守护进程会再主线程结束就立即结束 普通线程不会
        thread.setDaemon(true);
        thread.start();
        System.out.println("main end");
    }
}
