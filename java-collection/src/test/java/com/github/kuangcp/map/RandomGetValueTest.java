package com.github.kuangcp.map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-29-下午12:04
 * get random value from map
 */
@Slf4j
public class RandomGetValueTest {

  private Map<String, String> data = new HashMap<>();

  @Before
  public void init() {
    for (int i = 0; i < 3; i++) {
      data.put("_" + i, "_" + i);
    }
  }

  @Test
  public void testRandomValue() {
    String[] keys = new String[data.size()];
    data.keySet().toArray(keys);
    int index = ThreadLocalRandom.current().nextInt(keys.length);
    String result = data.get(keys[index]);
    log.debug("random: result={}", result);

    assertThat(data.values(), hasItem(result));
  }
}
