package com.github.kuangcp.map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class ConcurrentModificationTest {

    /**
     * 错误
     * <p>
     * HashMap中的实现是在需要做修改操作前缓存 modCount, 修改后再比对缓存值是否
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testHashMapIter() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        fillVal(map);
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> next = it.next();
            map.remove(next.getKey());
        }
    }

    /**
     * 正确
     */
    @Test
    public void testHashMapIterCorrect() {
        Map<Integer, String> map = new HashMap<>();
        fillVal(map);
        log.info("map={}", map);
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> next = it.next();
            if (next.getKey() > 1) {
                it.remove();
            }
        }
        log.info("map={}", map);
    }

    @Test
    public void testConcHashMapIter() throws Exception {
        // 不会抛出 ConcurrentModificationException，但是无法保证迭代时一致性
        Map<Integer, String> map = new ConcurrentHashMap<>();
        fillVal(map);
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> next = it.next();
            map.remove(next.getKey());
        }
        log.info("map={}", map);
    }

    private void fillVal(Map<Integer, String> map) {
        map.put(1, "1");
        map.put(2, "1");
        map.put(3, "1");
        map.put(4, "1");
    }
}
