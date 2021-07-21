package com.ppphuang.demo.synchronized1;

import java.util.concurrent.locks.LockSupport;

public class SyncPrint21Tow {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            LockSupport.park();
            System.out.println(1);
        });
        thread.start();
        new Thread(() -> {
            System.out.println(2);
            LockSupport.unpark(thread);
        }).start();
    }
}



