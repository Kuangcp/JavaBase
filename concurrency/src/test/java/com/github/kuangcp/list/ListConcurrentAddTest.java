package com.github.kuangcp.list;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import thread.pool.RecommendUsePool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Kuangcp
 * 2026-05-07 15:06
 */
@Slf4j
public class ListConcurrentAddTest {

    @RepeatedTest(value = 10)
    public void testLinkedListAddToBroke() throws Exception {
        List<Long> tar = new LinkedList<>();
        testForList(tar);
    }

    @RepeatedTest(value = 10)
    public void testArrayList() throws Exception {
        List<Long> tar = new ArrayList<>();
        testForList(tar);
    }

    private static void testForList(List<Long> tar) throws InterruptedException {
        int loop = 5;
        CountDownLatch latch = new CountDownLatch(loop);
        for (int j = 0; j < loop; j++) {
            RecommendUsePool.IO.ioPool.submit(() -> {
                try {
                    int size = ThreadLocalRandom.current().nextInt(50);
                    for (int i = 0; i < size; i++) {
                        tar.add(ThreadLocalRandom.current().nextLong(129873876782L));
                    }
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // LinkedList 链表结构被破坏，导致next.next 这里做遍历时会报NPE
        // ArrayList forEach 时也会NPE
        long s = 0L;
        for (Long l : tar) {
            s += l;
        }
        System.out.println(s);
    }
}
