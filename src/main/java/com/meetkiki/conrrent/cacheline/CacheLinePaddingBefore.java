package com.meetkiki.conrrent.cacheline;

import sun.misc.Contended;

public class CacheLinePaddingBefore {

    public static final int COUNT = 1_0000_0000;

    /**
     * 此注解有一个前提，必须开启JVM参数-XX:-RestrictContended，此注解才会生效
     *  可以发现注释后效率比注释前效率低了3倍左右 本质上是空间换时间的思想
     */
    @Contended
    private static class Entity {
        public volatile long x = 1L;
    }

    public static Entity[] arr = new Entity[2];

    static {
        arr[0] = new Entity();
        arr[1] = new Entity();
    }

    public static void main(String[] args) throws InterruptedException {

        Thread threadA = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr[0].x = i;
            }
        }, "ThreadA");

        Thread threadB = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr[1].x = i;
            }
        }, "ThreadB");

        final long start = System.nanoTime();
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        final long end = System.nanoTime();
        System.out.println("耗时：" + (end - start) / 100_0000);

    }
}