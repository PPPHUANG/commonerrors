package com.ppphuang.demo.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class InterruptState {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.info("start sleep ...");
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e) {
                    log.info("waak up");
                    e.printStackTrace();
                }
            }
        };
        //NEW
        System.out.println(t1.getState());
        t1.start();
        //RUNNABLE
        System.out.println(t1.getState());
        Thread.sleep(1000);
        //TIMED_WAITING
        System.out.println(t1.getState());
        t1.interrupt();
        //interrupt()方法去中断一个正在正在sleep的线程的时候，中断标志位先是true，
        // 然后标志位被清除并且抛异常。但是在中断thread线程的时候，
        // thread线程还没来得及抛异常并且清除中断标志位，主线程就调用isInterrupted()方法去打印中断标志，
        // 也就是说主线程最后一行的输出语句跑在了thread线程清除中断标志的前面。
        // 之前我还以为interrupt()方法有bug。要是中断后让主线程sleep一会儿，
        // 或者在catch语句中打印中断标志就没问题了，就会打印正确的结果false。
        // sleep join 打断是false。
        Thread.sleep(1000);
        System.out.println(t1.isInterrupted());
    }
}
