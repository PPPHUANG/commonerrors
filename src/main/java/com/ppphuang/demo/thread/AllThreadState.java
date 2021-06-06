package com.ppphuang.demo.thread;

public class AllThreadState {
    public static void main(String[] args) {
        //NEW
        Thread t1 = new Thread(() -> {
            System.out.println("NEW");
        },"t1");

        Thread t2 = new Thread(() -> {
            while (true){
                //RUNNABLE
            }
        },"t2");
        t2.start();

        Thread t3 = new Thread(() -> {
            System.out.println("running");
        },"t3");
        //TERMINATED
        t3.start();

        Thread t4 = new Thread(() -> {
            synchronized (AllThreadState.class) {
                try {
                    //TIMED_WAITING
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t4");
        t4.start();

        Thread t5 = new Thread(() -> {
            try {
                //WAITING
                t2.join();
                //TIMED_WAITING
//                t2.join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t5");
        t5.start();

        Thread t6 = new Thread(() -> {
            synchronized (AllThreadState.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t6");
        //BLOCKED
        t6.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("t1 state " + t1.getState());
        System.out.println("t2 state " + t2.getState());
        System.out.println("t3 state " + t3.getState());
        System.out.println("t4 state " + t4.getState());
        System.out.println("t5 state " + t5.getState());
        System.out.println("t6 state " + t6.getState());
//        t1 state NEW
//        t2 state RUNNABLE
//        t3 state TERMINATED
//        t4 state TIMED_WAITING
//        t5 state WAITING
//        t6 state BLOCKED
    }
}
