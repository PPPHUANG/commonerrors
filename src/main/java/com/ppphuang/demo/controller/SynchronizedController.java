package com.ppphuang.demo.controller;

import com.ppphuang.demo.dao.CounterData;
import com.ppphuang.demo.dao.CounterDataNew;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class SynchronizedController {
    private ConcurrentHashMap<String, Item> items = new ConcurrentHashMap<>();

    public SynchronizedController() {
        IntStream.range(0, 10).forEach(i -> items.put("item" + i, new Item("item" + i)));
    }

    //    在非静态的 wrong 方法上加锁，只能确保多个线程无法执行同一个实例的 wrong 方法，
    //    却不能保证不会执行不同实例的 wrong 方法。而静态的 counter 在多个实例中共享，所以必然会出现线程安全问题。
    @GetMapping("/synchronized/wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        CounterData.reset();
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new CounterData().wrong());
        return CounterData.getCounter();
        // 结果 363878
    }

    //    理清思路后，修正方法就很清晰了：同样在类中定义一个 Object 类型的静态字段，在操作 counter 之前对这个字段加锁。
    @GetMapping("/synchronized/right")
    public int right(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new CounterDataNew().right());
        return CounterDataNew.getCounter();
        // 结果 1000000
    }


    private List<Integer> data = new ArrayList<>();

    //不涉及共享资源的慢方法
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
    }

    //错误的加锁方法
    @GetMapping("granularity/wrong")
    public int wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            //加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }

    //正确的加锁方法
    @GetMapping("granularity/right")
    public int right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            //只对List加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }




    private List<Item> createCart() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(items.size()))
                .map(name -> items.get(name)).collect(Collectors.toList());
//        return IntStream.rangeClosed(1, 3)
//                .mapToObj(name -> items.get("item" + ThreadLocalRandom.current().nextInt(items.size()))).collect(Collectors.toList());
//        List<Integer> a = IntStream.rangeClosed(1, 3)
//                .map(name -> name + 1).boxed().collect(Collectors.toList());
    }


    private boolean createOrder(List<Item> order) {
        //存放所有获得的锁
        List<ReentrantLock> locks = new ArrayList<>();

        for (Item item : order) {
            try {
                //获得锁10秒超时
                if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
                    locks.add(item.lock);
                } else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (InterruptedException e) {
            }
        }
        //锁全部拿到之后执行扣减库存业务逻辑
        try {
            order.forEach(item -> item.remaining--);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return true;
    }


    @GetMapping("deadLock/wrong")
    public long deadLockWrong() {
        long begin = System.currentTimeMillis();
        //并发进行100次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }
//    success:55 totalRemaining:9835 took:90032ms items:{
//        item0=SynchronizedController.Item(name=item0, remaining=984),
//        item2=SynchronizedController.Item(name=item2, remaining=986),
//        item1=SynchronizedController.Item(name=item1, remaining=984),
//        item8=SynchronizedController.Item(name=item8, remaining=993),
//        item7=SynchronizedController.Item(name=item7, remaining=981),
//        item9=SynchronizedController.Item(name=item9, remaining=983),
//        item4=SynchronizedController.Item(name=item4, remaining=988),
//        item3=SynchronizedController.Item(name=item3, remaining=983),
//        item6=SynchronizedController.Item(name=item6, remaining=978),
//        item5=SynchronizedController.Item(name=item5, remaining=975)
//    }


    @GetMapping("deadLock/right")
    public long deadLockRight() {
        long begin = System.currentTimeMillis();
        //并发进行100次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream()
                            .sorted(Comparator.comparing(Item::getName))
                            .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }

    @Data
    @RequiredArgsConstructor
    static class Item {
        final String name; //商品名
        int remaining = 1000; //库存剩余
        @ToString.Exclude //ToString不包含这个字段
        ReentrantLock lock = new ReentrantLock();
    }
}
