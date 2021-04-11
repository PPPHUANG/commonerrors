package com.ppphuang.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@SpringBootTest
class BusinessErrorApplicationTests {

    @Test
    void contextLoads() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(System.out::println));
        //查询还需要补充多少个元素

    }

}
