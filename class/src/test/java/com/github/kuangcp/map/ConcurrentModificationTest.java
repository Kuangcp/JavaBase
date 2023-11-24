package com.github.kuangcp.map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO 完成
 *
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class ConcurrentModificationTest {

    /**
     * 异常时而发生时而不发生, 因为不像List, Map是无序的, HashMap中的实现是在需要做修改操作前缓存 modCount, 修改后再比对缓存值是否
     */
//  @Test(expected = ConcurrentModificationException.class)
    @Test
    public void testHashMap() {
        Map<String, String> map = new HashMap<>();

        log.info("foreach");
        testReadAndModifyMapByForEach(map);

        log.info("lambda");
        readAndModifyMapByLambda(map);
    }

    // TODO 使用了ConcurrentHash 后避免了异常, 但是每次迭代的, 是最新的数据么
    @Test
    public void testConcurrentHashMap() {
        Map<String, String> map = new ConcurrentHashMap<>();

        log.info("foreach");
        testReadAndModifyMapByForEach(map);

        log.info("lambda");
        readAndModifyMapByLambda(map);
    }

    /**
     * 一条线程增加 主线程 forEach 读取
     */
    private void testReadAndModifyMapByForEach(Map<String, String> map) {
        new Thread(() -> addItemToMap(map)).start();

        for (int i = 0; i < 100; i++) {
            log.info("read total: i={} size={}", i, map.size());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("", e);
            }

            for (String value : map.values()) {
                String temp = value.replace("1", "ddd");
            }
        }
    }

    /**
     * 一条线程增加 主线程通过 Lambda 表达式读取
     */
    private void readAndModifyMapByLambda(Map<String, String> map) {
        new Thread(() -> addItemToMap(map)).start();
        new Thread(() -> removeItemFromMap(map)).start();

        for (int i = 0; i < 300; i++) {
            log.info("read total: i={} size={}", i, map.size());
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            map.values().forEach(value -> {
                String temp = value.replace("1", "ddd");
            });
        }
    }

    private void addItemToMap(Map<String, String> map) {
        for (int i = 0; i < 300; i++) {
            log.info("addItemToMap: i={}", i);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            map.put("t " + i, "1");
        }
    }

    private void removeItemFromMap(Map<String, String> map) {
        for (int i = 0; i < 300; i++) {
            log.info("addItemToMap: i={}", i);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            map.remove("t " + i);
        }
    }
}
