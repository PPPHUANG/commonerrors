package com.ppphuang.demo.jmm.gc;

public class MaxTenuringThreshold {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:Survivor-
     * Ratio=8 -XX:MaxTenuringThreshold=1
     * -XX:+PrintTenuringDistribution  JVM 在每次新生代GC时，打印出幸存区中对象的年龄分布。
     */
    public static void main(String[] args) {
        testTenuringThreshold();
    }

    @SuppressWarnings("unused")
    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        // 什么时候进入老年代决定于XX:MaxTenuring-Threshold设置
        allocation1 = new byte[_1MB / 4];
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }
}
