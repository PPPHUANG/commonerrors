package com.ppphuang.demo;

import com.ppphuang.demo.dao.mappers.test.AddressPOMapper;
import com.ppphuang.demo.dao.mappers.test.UserPOMapper;
import com.ppphuang.demo.dao.model.test.AddressPO;
import com.ppphuang.demo.dao.model.test.UserPO;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@SpringBootTest
class BusinessErrorApplicationTests {
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

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

    @Test
    void zookeeperConfigSender() throws Exception {
        final String remoteAddress = "127.0.0.1:2128";
        final String rule = "[\n"
                + "  {\n"
                + "    \"resource\": \"HelloWorld\",\n"
                + "    \"controlBehavior\": 0,\n"
                + "    \"count\": 1.0,\n"
                + "    \"grade\": 1,\n"
                + "    \"limitApp\": \"default\",\n"
                + "    \"strategy\": 0\n"
                + "  }\n"
                + "]";

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress, new ExponentialBackoffRetry
                (SLEEP_TIME, RETRY_TIMES));
        zkClient.start();
        String appName = "sentinelclient";
        String path = "/sentinel_rule_config/" + appName;
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, rule.getBytes());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zkClient.close();
    }
}
