package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockTimeOut {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("尝试获得锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    System.out.println("尝试获得锁失败");
                    return;//没有获得锁 需要返回，不能执行后续获得锁才能执行的代码
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;//没有获得锁 需要返回，不能执行后续获得锁才能执行的代码
            }
            try{
                System.out.println("获得锁");
            }finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
