package com.ppphuang.demo.thread;

public class YieldPriority {
    public static void main(String[] args) {
        Runnable task1 = ()->{int count = 0;while (true){
            System.out.println("---->1" + count++);
        }};
        Runnable task2 = ()->{int count = 0;while (true){
            Thread.yield();
            System.out.println("---->2" + count++);
        }};
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
//        t1.setPriority(Thread.MIN_PRIORITY);
//        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
        //结果看出 yield 和 setPriority 对时间片有一定作用
    }
}
