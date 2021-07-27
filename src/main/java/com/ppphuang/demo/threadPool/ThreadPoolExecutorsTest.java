package com.ppphuang.demo.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPoolExecutorsTest {
    public static void main(String[] args) {
        //固定线程数的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            log.info("1");
        });
        executorService.execute(() -> {
            log.info("2");
        });
        executorService.execute(() -> {
            log.info("3");
        });
        //固定线程数 自定义线程工厂的线程池
        ExecutorService executorService1 = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger num = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mypool_t" + num.getAndIncrement());
            }
        });
        executorService1.execute(() -> {
            log.info("1");
        });
        executorService1.execute(() -> {
            log.info("2");
        });
        executorService1.execute(() -> {
            log.info("3");
        });

        //带缓冲的线程池 core线程为0 最大线程为int 同步队列 一手交钱一手交货 适合任务多时间短的场景
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        executorService2.execute(() -> {
            log.info("1");
        });

        ExecutorService executorService3 = Executors.newSingleThreadExecutor();
        executorService3.execute(() -> {
            log.info("1");
            int i = 1 / 0;
        });
        executorService3.execute(() -> {
            log.info("2");
        });
        executorService3.execute(() -> {
            log.info("3");
        });
    }
}
