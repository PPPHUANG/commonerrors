package com.ppphuang.demo.reentrantLock;

/**
 * 两个线程互相改变退出条件称之为 活锁
 */
public class LiveLock {
    static volatile int count = 10;
    public static void main(String[] args) {
        new Thread(() -> {
            while (count  > 0) {
                try {
                    Thread.sleep(200);
                    count--;
                    System.out.println("count:" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (count  < 20) {
                try {
                    Thread.sleep(200);
                    count++;
                    System.out.println("count:" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
