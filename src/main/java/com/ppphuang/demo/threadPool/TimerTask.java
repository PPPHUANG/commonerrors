package com.ppphuang.demo.threadPool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimerTask {
    public static void main(String[] args) {
//        extracted();
        //延时执行
//        extracted1();
        //调度执行
//        extracted2();
        //定时执行
        extracted3();
    }

    private static void extracted3() {
        //每周四16点执行任务
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime time = now.withHour(16).withSecond(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);

        if (now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }
        long initailDelay = Duration.between(now, time).toMillis();
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            System.out.println("running");
        }, initailDelay, period, TimeUnit.MILLISECONDS);
    }

    private static void extracted2() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.debug("task1");
            try {
//                Thread.sleep(1000);
                //执行时间超过调度时间周期时，任务会挨着执行，没有一秒的调度时间
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.debug("task2");
            try {
//                Thread.sleep(1000);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //任务间隔1秒执行
        }, 1, 1, TimeUnit.SECONDS);
    }

    private static void extracted1() {
        //        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        //单个线程时会同步执行
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() -> {
            log.debug("task1");
            try {
                Thread.sleep(1000);
                //第一个任务执行异常不会影响后续任务执行
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(() -> {
            log.debug("task2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static void extracted() {
        Timer timer = new Timer();
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("task 1");
                Thread.sleep(1000);
                //没有正确捕获异常后续任务不会执行
//                int i =1 / 0;
            }
        };
        java.util.TimerTask timerTask1 = new java.util.TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
            }
        };
        //同时提交两个会同步执行
        timer.schedule(timerTask, 1000);
        timer.schedule(timerTask1, 1000);
    }
}
