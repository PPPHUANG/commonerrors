package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁 synchronize也是可重入
 */
public class ReEnterLock {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            System.out.println("main");
            m();
        }finally {
            lock.unlock();
        }
    }

    public static void m() {
        lock.lock();
        try {
            System.out.println("m");
            m1();
        }finally {
            lock.unlock();
        }
    }
    public static void m1() {
        lock.lock();
        try {
            System.out.println("m1");
        }finally {
            lock.unlock();
        }
    }
}
