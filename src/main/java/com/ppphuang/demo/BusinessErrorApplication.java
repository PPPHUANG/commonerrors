package com.ppphuang.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BusinessErrorApplication {

    public static void main(String[] args) {
        //在初始化中调用方法和增加JVM停机钩子方法
        //增加停机脚本
        //#!/usr/bin/env bash
        //ps -ef|grep "projectName"  | grep -v grep |awk '{print $2}'|xargs kill -15


        //kill -9 PID 是操作系统从内核级别强制杀死一个进程.
        //kill -15 PID 可以理解为操作系统发送一个通知告诉应用主动关闭.
        //SIGNTERM（15） 的效果是正常退出进程，退出前可以被阻塞或回调处理。并且它是Linux缺省的程序中断信号。
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("showdown 4 3 2 1")));
        SpringApplication.run(BusinessErrorApplication.class, args);
    }

}
