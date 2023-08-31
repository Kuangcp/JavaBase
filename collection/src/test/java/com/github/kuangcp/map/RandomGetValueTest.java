package com.github.kuangcp.map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.kuangcp.mock.MockMap;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-29-下午12:04
 * get random value from map
 */
@Slf4j
public class RandomGetValueTest {

    private Map<Integer, Integer> data = MockMap.mock(5, 100, 10);

    @Test
    public void testRandomValue() {
        Integer[] keys = new Integer[data.size()];
        data.keySet().toArray(keys);
        int index = ThreadLocalRandom.current().nextInt(keys.length);
        Integer result = data.get(keys[index]);
        log.debug("random: result={}", result);

        assertThat(data.values(), hasItem(result));
    }
}
