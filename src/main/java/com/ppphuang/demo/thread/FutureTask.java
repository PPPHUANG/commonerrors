package com.ppphuang.demo.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class FutureTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        java.util.concurrent.FutureTask<Integer> hello = new java.util.concurrent.FutureTask<>(() -> {
            log.debug("hello");
            return 100;
        });
        Thread t1 = new Thread(hello, "t1");
        t1.start();
        //hello.get()会阻塞等待结果
        System.out.println(hello.get());
    }
}
