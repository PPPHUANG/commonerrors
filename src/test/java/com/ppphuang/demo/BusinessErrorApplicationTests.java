package com.ppphuang.demo;

import com.ppphuang.demo.dao.mappers.test.AddressPOMapper;
import com.ppphuang.demo.dao.mappers.test.UserPOMapper;
import com.ppphuang.demo.dao.model.test.AddressPO;
import com.ppphuang.demo.dao.model.test.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@SpringBootTest
class BusinessErrorApplicationTests {

    @Autowired
    UserPOMapper userPOMapper;

    @Autowired
    AddressPOMapper addressPOMapper;

    @Test
    void contextLoads() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(System.out::println));
        //查询还需要补充多少个元素


    }

    @Test
    void test() {
//        AddressPO address = new AddressPO();
//        address.setAddress("\uD83D\uDE00awesome");
//        addressPOMapper.insertSelective(address);
        AddressPO address = addressPOMapper.selectByPrimaryKey(1);
        System.out.println(address.getAddress());
    }

}
