package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class TryLock {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("尝试获得锁");
            if (!lock.tryLock()) {
                System.out.println("尝试获得锁失败");
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
    }
}
