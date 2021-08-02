package com.ppphuang.demo.thread;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
//        test();
//        test1();
        test2();
    }

    private static void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        String[] all = new String[10];
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.submit(() -> {
                for (int j = 0; j <= 100; j++) {
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[finalI] = j + "%";
                    System.out.print("\r" + Arrays.toString(all));
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("任务结束");
        executorService.shutdownNow();
    }

    private static void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        executorService.submit(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        });
        executorService.submit(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        });
        executorService.submit(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        });
        executorService.submit(() -> {
            try {
                countDownLatch.await();
                log.debug("await");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            log.debug("bengin");
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
    }
}
