package com.github.kuangcp.map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Kuangcp
 * 2024-04-07 15:28
 */
@Slf4j
public class ConcurrentSkipListMapTest {

    /**
     * 可指定key的比较器实现 默认用key对应类型的实现
     */
    Map<String, String> map = new ConcurrentSkipListMap<>();

    @Test
    public void testNormal() throws Exception {
        Map<String, String> hash = new ConcurrentHashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(i + "", i + "");
            hash.put(i + "", i + "");
        }
        log.info("map={}", map);
        log.info("hash={}", hash);
    }
}
