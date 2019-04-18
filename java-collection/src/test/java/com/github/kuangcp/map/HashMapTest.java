package com.github.kuangcp.map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.HashMap;
import java.util.UUID;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-16 10:22 AM
 */
public class HashMapTest {

  @Test
  public void testPut() {
    HashMap<String, String> map = new HashMap<>(3); // 初始大小4 扩容阈值为 3 大于才扩容
    for (int i = 0; i < 100; i++) {
      String key = i + "  " + UUID.randomUUID().toString();
      map.put(key, "  ");
      int i1 = key.hashCode();
      map.size();
    }
  }

  static int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }

  @Test
  public void testR() {
    int value = 0b101010;
    // 101010
    // 000101
    // 101111
    assertThat(value ^ (value >>> 3), equalTo(0b101111));

    assertThat(  0b10111010111101110101001100000011
            ^ 0b00000000000000001011101011110111,
        equalTo(0b10111010111101111110100111110100));
  }
}
