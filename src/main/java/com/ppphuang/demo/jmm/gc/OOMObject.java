package com.ppphuang.demo.jmm.gc;

import java.util.ArrayList;
import java.util.List;

public class OOMObject {
    /**
     * JConsole查看内存
     * 内存占位符对象，一个OOMObject大约占64KB
     * -Xms100m -Xmx100m -XX:+UseSerialGC
     */
    static class OOMObjecta {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObjecta> list = new ArrayList<OOMObjecta>();
        for (int i = 0; i < num; i++) {
            // 稍作延时，令监视曲线的变化更加明显
            Thread.sleep(500);
            list.add(new OOMObjecta());
        }
        System.gc();
//        为何执行了System.gc()之后，老年代的柱状图仍然显示峰值状态，
//        代码需要如何调整才能让System.gc()回收掉填充到堆中的对象？
    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000);
//        System.gc();
//        执行System.gc()之后，空间未能回收是因为List<OOMObject>list对象仍然存活，
//        fillHeap()方法仍然没有退出，因此list对象在System.gc()执行时仍然处于作用域之内[1]。
//        如果把System.gc()移动到fillHeap()方法外调用就可以回收掉全部内存。
    }
}

