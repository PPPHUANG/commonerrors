package com.ppphuang.demo.synchronized1;

import java.util.concurrent.locks.LockSupport;

public class ParkRepeatABC {
    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {
        Park park = new Park(5);
        t1 = new Thread(()->{park.print("a",t2);});
        t2 = new Thread(()->{park.print("b",t3);});
        t3 = new Thread(()->{park.print("c",t1);});
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}

class Park{
    private int loopNum;

    public Park( int loopNum) {
        this.loopNum = loopNum;
    }

    public void print(String str, Thread nextThread) {
        for (int i = 0; i < loopNum; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(nextThread);
        }
    }
}