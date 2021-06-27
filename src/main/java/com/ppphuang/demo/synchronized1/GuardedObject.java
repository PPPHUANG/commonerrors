package com.ppphuang.demo.synchronized1;

import java.util.Date;

public class GuardedObject {
    public static void main(String[] args) {
        ThisGuardedObject thisGuardedObject = new ThisGuardedObject();
        Thread thread1 = new Thread(() -> {
            Object o = thisGuardedObject.get();
            System.out.println(o);
        }, "t1");
        Thread thread2 = new Thread(() -> {
            thisGuardedObject.complete("this is reponse");
        }, "t2");
        thread1.start();
        thread2.start();
    }
}

class ThisGuardedObject {
    private Object response;

    public Object get() {
        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public Object get(long timeout) {
        long startTime = System.currentTimeMillis();
        long passedTime = 0;
        synchronized (this) {
            while (response == null) {
                long waitTime = timeout - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - startTime;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
