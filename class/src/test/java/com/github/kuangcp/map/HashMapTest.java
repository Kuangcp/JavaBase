package com.github.kuangcp.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author kuangcp on 2019-04-16 10:22 AM
 */
@Slf4j
public class HashMapTest {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Banana {

        private String tag;

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return tag;
        }
    }

    /**
     * 由于hashCode都是1, 就形成了 table[1] 这个单链表, 但是不影响正常使用
     */
    @Test
    public void testSameHashCode() {
        Map<Banana, Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(new Banana(), 1);
        }

        Banana banana = Banana.builder().tag("banana").build();
        map.put(banana, 12);
        map.remove(new Banana());

        System.out.println("size=" + map.size());
        map.keySet().forEach(System.out::println);
    }

    /**
     * 注意 keySet 返回的是 Map 中的存储结构, 如果改变 keySet, 就会影响到原有的Map
     */
    @Test
    public void testKeySet() {
        Map<String, String> dict1 = new HashMap<>();
        Map<String, String> dict2 = new HashMap<>();

        dict1.put("1", "5");
        dict1.put("2", "5");
        dict1.put("3", "5");

        dict2.put("4", "5");
        dict2.put("3", "5");
        dict2.put("2", "5");

        log.info("new set");

        HashSet<String> common = new HashSet<>(dict1.keySet());
        common.retainAll(dict2.keySet());
        assertThat(common.size(), equalTo(2));
        assertThat(dict1.size(), equalTo(3));
        assertThat(dict2.size(), equalTo(3));
        showMap(dict1);

        log.info("origin set");
        dict1.keySet().retainAll(dict2.keySet());
        assertThat(dict1.size(), equalTo(2));
        assertThat(dict2.size(), equalTo(3));

        showMap(dict1);
    }

    private <K, V> void showMap(Map<K, V> map) {
        if (Objects.isNull(map)) {
            return;
        }

        map.forEach((k, v) -> log.info("k={} v={}", k, v));
    }

    @Test
    public void testStackOverFlow() throws Exception {
        HashMap<String, Object> testMap = new HashMap<String, Object>();
        testMap.put("me", testMap);
        HashMap<Object, Object> testMap2 = new HashMap<Object, Object>();
        testMap2.put(testMap, null); // <---- causes a stack overflow.

    }
}
