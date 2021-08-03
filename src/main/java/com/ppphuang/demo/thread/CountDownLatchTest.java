package com.ppphuang.demo.thread;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        test();
//        test1();
//        test2();
//        test3();
        test4();
    }

    private static void test4() {
        CyclicBarrier cb = new CyclicBarrier(2); // 个数为2时才会继续执行
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println("线程1开始.." + new Date());
                try {
                    cb.await(); // 当个数不足时，等待
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1继续向下运行..." + new Date());
            }).start();
            new Thread(() -> {
                System.out.println("线程2开始.." + new Date());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                try {
                    cb.await(); // 2 秒后，线程个数够2，继续运行
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2继续向下运行..." + new Date());
            }).start();
        }
    }

    private static void test3() throws ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<Map> submit = executorService.submit(() -> {
            Map local = restTemplate.getForObject("http://localhost:8080/get1", Map.class);
            return local;
        });
        Future<Map> submit1 = executorService.submit(() -> {
            Map local = restTemplate.getForObject("http://localhost:8080/get2", Map.class);
            return local;
        });
        Future<Map> submit2 = executorService.submit(() -> {
            Map local = restTemplate.getForObject("http://localhost:8080/get3", Map.class);
            return local;
        });
        System.out.println(submit.get());
        System.out.println(submit1.get());
        System.out.println(submit2.get());
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
