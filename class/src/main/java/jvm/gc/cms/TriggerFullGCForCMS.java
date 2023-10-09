package jvm.gc.cms;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-05-15 18:53
 * <p>
 * 问题：内存溢出后会死循环创建 dump 直到填满硬盘，CMSScavengeBeforeRemark 并不能在 CMSGC 的时候触发 FullGC
 * <p>
 * -Xms300m -Xmx300m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=1
 * -XX:CMSInitiatingOccupancyFraction=75 -XX:+CMSScavengeBeforeRemark -XX:+HeapDumpBeforeFullGC
 * -XX:+HeapDumpOnOutOfMemoryError -XX:+CMSClassUnloadingEnabled -XX:+DisableExplicitGC -verbose:gc
 * -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:HeapDumpPath=/home/kcp/test/java-heapdump.hprof
 * -Xloggc:/home/kcp/test/gc.log
 */
@Slf4j
public class TriggerFullGCForCMS {

    final static Runnable eatMem = () -> {
        List<Object> temp = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            temp.add(new byte[1024 * 1024]);
            System.out.println(Thread.currentThread().getName() + "  " + i);
        }
    };

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Object> live = new ArrayList<>();
        while (true) {
            pool.execute(eatMem);
            live.add(new byte[1024 * 1024]);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
