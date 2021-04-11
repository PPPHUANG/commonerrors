package com.ppphuang.demo.dao;

public class CounterDataNew {
    private static int counter = 0;
    private static Object locker = new Object();
    public void right() {
        synchronized (locker) {
            counter++;
        }
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        CounterDataNew.counter = counter;
    }
}
