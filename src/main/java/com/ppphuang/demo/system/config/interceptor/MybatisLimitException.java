package com.ppphuang.demo.system.config.interceptor;

public class MybatisLimitException extends Exception {

    private String message;

    //提供有参构造
    public MybatisLimitException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}