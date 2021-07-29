package com.ppphuang.demo.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class PoolMethodTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("1");
                return "ok";
            }
        });
        log.info(submit.get());

        Future<String> submit1 = executorService.submit(() ->{
                log.info("2");
                return "ok2";
        });
        log.info(submit1.get());

        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        List<Future<String>> futures = executorService1.invokeAll(Arrays.asList(
                () -> {
                    log.info("11");
                    return "ok11";
                },
                () -> {
                    log.info("22");
                    return "ok22";
                }, () -> {
                    log.info("3");
                    return "ok3";
                }));
        futures.forEach(i -> {
            try {
                log.info(i.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);
        String res = executorService2.invokeAny(Arrays.asList(
                () -> {
                    log.info("start11");
                    Thread.sleep(1000);
                    log.info("end11");
                    return "ok11";
                },
                () -> {
                    log.info("start22");
                    Thread.sleep(2000);
                    log.info("end22");
                    return "ok22";
                }, () -> {
                    log.info("start33");
                    Thread.sleep(3000);
                    log.info("end33");
                    return "ok3";
                }));
        System.out.println(res);
    }
}
