package com.ppphuang.demo.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐死锁问题
 */
public class SDeadLock {
    public static void main(String[] args) {
        SChopstick c1 = new SChopstick("1");
        SChopstick c2 = new SChopstick("2");
        SChopstick c3 = new SChopstick("3");
        SChopstick c4 = new SChopstick("4");
        SChopstick c5 = new SChopstick("5");
        new SPhilosopher("苏格拉底",c1,c2).start();
        new SPhilosopher("柏拉图",c2,c3).start();
        new SPhilosopher("亚里士多德",c3,c4).start();
        new SPhilosopher("赫拉克利特",c4,c5).start();
        new SPhilosopher("阿基米德",c5,c1).start();
    }
}

class SPhilosopher extends Thread{
    SChopstick left;
    SChopstick right;

    public SPhilosopher(String name, SChopstick left, SChopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            if(left.tryLock()){
                try {
                    if(right.tryLock()){
                        try {
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }
                }finally {
                    left.unlock();
                }
            }
//            synchronized (left) {
//                synchronized (right) {
//                    eat();
//                }
//            }
        }
    }
    public void eat() {
        System.out.println(getName() + "eating");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SChopstick extends ReentrantLock {
    String name;

    public SChopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SChopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}
