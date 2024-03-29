package com.github.kuangcp.map;

import com.github.kuangcp.mock.MockMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp on 17-8-21  下午2:48
 * Map 的常见迭代用法
 */
@Slf4j
public class IterateMapTest {

    private static Map<String, String> map = MockMap.mock(6, String.class, String.class);

    /**
     * 通过Map.keySet()遍历key和value,二次取值
     */
    @Test
    public void loopByKeySet() {
        for (String key : map.keySet()) {
            log.debug("{}:{}", key, map.get(key));
        }
    }

    /**
     * 通过Map.entrySet()遍历key和value(推荐使用)
     */
    @Test
    public void loopByEntrySet() {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            log.debug("{}:{}", entry.getKey(), entry.getValue());
        }
    }

    /**
     * 通过Map.entrySet()使用iterator()遍历key和value
     * idea 都会提出警告, 说明了这种方式不是很好的遍历方式
     */
    @Test
    public void loopByIterator() {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            log.debug("{}:{}", entry.getKey(), entry.getValue());
        }
    }
}
