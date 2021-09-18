package com.ppphuang.demo.designpattern.chainofresponsibility;

/**
 * 创建扩展了该记录器类的实体类
 */
public class ConsoleLogger extends AbstractLogger {
    public ConsoleLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Standard Console::Logger: " + message);
    }
}
