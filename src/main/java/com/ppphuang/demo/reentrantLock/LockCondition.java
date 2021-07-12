package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockCondition {
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock room = new ReentrantLock();
    static Condition waitCigaretteSet = room.newCondition();
    static Condition waitTakeoutSet = room.newCondition();

    public static void main(String[] args) {
        new Thread(()-> {
            room.lock();
            try {
                while (!hasCigarette) {
                    System.out.println("没烟休息会");
                    try {
                        waitCigaretteSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("有烟可以开始干活了");
            }finally {
                room.unlock();
            }
        },"小丽").start();
        new Thread(()-> {
            room.lock();
            try {
                while (!hasTakeout) {
                    System.out.println("没外卖休息会");
                    try {
                        waitTakeoutSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("有外卖可以开始干活了");
            }finally {
                room.unlock();
            }
        },"小花").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            room.lock();
            try {
                hasCigarette = true;
                waitCigaretteSet.signal();
            }finally {
                room.unlock();
            }
        },"烟").start();
        new Thread(()->{
            room.lock();
            try {
                hasTakeout = true;
                waitTakeoutSet.signal();
            }finally {
                room.unlock();
            }
        },"外卖").start();
    }
}

