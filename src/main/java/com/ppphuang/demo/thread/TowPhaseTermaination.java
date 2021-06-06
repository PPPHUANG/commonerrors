package com.ppphuang.demo.thread;

import sun.awt.windows.ThemeReader;

public class TowPhaseTermaination {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();
        Thread.sleep(3500);
        monitor.stop();
    }
}
class Monitor {
    private Thread monitor;
    public void start () {
        monitor = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    System.out.println("打断结束");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    System.out.println("正在监控");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //重新设置打断标记 因为sleep被打断是false 正常代码打断是true
                    currentThread.interrupt();
                }
            }
        });
        monitor.start();
    }
    public void stop () {
        monitor.interrupt();
    }
}