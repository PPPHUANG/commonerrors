package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  可打断锁
 */
public class InterruptedLock {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("尝试获得锁lockInterrupt");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("没获得锁lockInterrupt，被打断");
                //没有获得锁 需要返回，不能执行后续获得锁才能执行的代码
                return;
            }

//            System.out.println("尝试获得锁lockInterrupt");
//            //lock.lock() 不能被打断
//            lock.lock();

            try {
                System.out.println("获得锁lockInterrupt");
            }finally {
                lock.unlock();
            }
        }, "T1");
        lock.lock();
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
    }
}
