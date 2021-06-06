package com.ppphuang.demo.thread;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class Join {
    static int i = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1"){
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(2);
                i = 10;
                System.out.println("thread end");
            }
        };
        t1.start();
        //在main线程中执行
        //t1.run();
        //阻塞同步等待
//        t1.join();
        //阻塞同步等待1.5秒
//        t1.join(1500);
        System.out.println(i);
    }
}
