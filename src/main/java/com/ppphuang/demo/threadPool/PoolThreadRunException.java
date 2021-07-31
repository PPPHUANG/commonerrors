package com.ppphuang.demo.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class PoolThreadRunException {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //自行捕获异常
//        extracted();
        //使用submit  callable 来接受运行中异常
        extracted1();
    }

    private static void extracted1() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Future<Boolean> task1 = scheduledExecutorService.submit(() -> {
            log.debug("task1");
            int i = 1 / 0;
            return true;
        });
        System.out.println(task1.get());
    }

    private static void extracted() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() -> {
            log.debug("task1");
            try {
                Thread.sleep(1000);
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
    }

}
