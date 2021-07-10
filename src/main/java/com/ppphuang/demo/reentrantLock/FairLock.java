package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class FairLock {
    private static final ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) {
        lock.lock();
        try {
            System.out.println("公平锁");
        }finally {
            lock.unlock();
        }
    }
}
