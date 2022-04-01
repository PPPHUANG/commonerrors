package com.ppphuang.demo.reflect.javassist.serverproxy;

/**
 * javassist是运行时，没有jdk编译过程，不支持自动拆装箱，需要对应基本类型的构造方法
 */
public class Response {
    Object data;

    public Response(Object data) {
        setData(data);
    }

    public Response(int data) {
        setData(data);
    }

    public Response(long data) {
        setData(data);
    }

    public Response(short data) {
        setData(data);
    }

    public Response(float data) {
        setData(data);
    }

    public Response(boolean data) {
        setData(data);
    }

    public Response(double data) {
        setData(data);
    }

    public Response(char data) {
        setData(data);
    }

    public Response(byte data) {
        setData(data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
