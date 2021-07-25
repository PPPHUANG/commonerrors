package com.ppphuang.demo.jmm;

import java.util.concurrent.atomic.AtomicInteger;

public class CASLock {
    public static void main(String[] args) {
        LockCAS lockCAS = new LockCAS();
        new Thread(() -> {
            lockCAS.lock();
            System.out.println("lock");
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lockCAS.unlock();
            }
        }).start();
        new Thread(() -> {
            lockCAS.lock();
            System.out.println("lock1");
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lockCAS.unlock();
            }
        }).start();
    }
}

class LockCAS {
    /**
     * 0 无锁 1加锁
     */
    private AtomicInteger state = new AtomicInteger(0);

    public void lock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    public void unlock() {
        state.set(0);
    }
}
