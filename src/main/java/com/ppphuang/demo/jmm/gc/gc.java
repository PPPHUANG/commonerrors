package com.ppphuang.demo.jmm.gc;

public class gc {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     */
//    尝试分配三个2MB大小和一个4MB大小的对象，在运行时通过-Xms20M、-Xmx20M、-Xmn10M这三个参数
//    限制了Java堆大小为20MB，不可扩展，其中10MB分配给新生代，剩下的10MB分配给老年代。
//    -XX：Survivor-Ratio=8决定了新生代中Eden区与一个Survivor区的空间比例是8∶1，
//    从输出的结果也清晰地看到“eden space 8192K、from space 1024K、tospace 1024K”的信息，
//    新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）。
    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        //出现一次Minor Gc
        allocation4 = new byte[4 * _1MB];
    }
//    执行testAllocation()中分配allocation4对象的语句时会发生一次Minor GC，
//    这次回收的结果是新生代6651KB变为148KB，而总内存占用量则几乎没有减少
//    （因为allocation1、2、3三个对象都是存活的，虚拟机几乎没有找到可回收的对象）。
//    产生这次垃圾收集的原因是为allocation4分配内存时，发现Eden已经被占用了6MB，
//    剩余空间已不足以分配allocation4所需的4MB内存，因此发生Minor GC。
//    垃圾收集期间虚拟机又发现已有的三个2MB大小的对象全部无法放入Survivor空间（Survivor空间只有1MB大小），
//    所以只好通过分配担保机制提前转移到老年代去。
}
