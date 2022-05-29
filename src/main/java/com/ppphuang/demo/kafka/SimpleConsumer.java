package com.ppphuang.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SimpleConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put("group.id", "gp-test-group");
        // 是否自动提交偏移量，只有commit之后才更新消费组的 offset
        props.put("enable.auto.commit", "true");
        // 消费者自动提交的间隔
        props.put("auto.commit.interval.ms", "1000");
        // 从最早的数据开始消费 earliest | latest | none
        props.put("auto.offset.reset", "earliest");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        // 订阅topic
        consumer.subscribe(Arrays.asList("first-topic"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d ,key =%s, value= %s, partition= %s%n", record.offset(), record.key(), record.value(), record.partition());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
