package com.github.kuangcp.map;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-16 10:22 AM
 */
public class HashMapTest {

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class Foo {

    private String name;

    @Override
    public int hashCode() {
      return 1;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  /**
   * 由于hashCode都是1, 就形成了 table[1] 这个单链表, 但是不影响正常使用
   */
  @Test
  public void testSameHashCode() {
    Map<Foo, Integer> map = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      map.put(new Foo(), 1);
    }

    Foo you = Foo.builder().name("you").build();
    map.put(you, 12);
    map.remove(new Foo());

    System.out.println("size=" + map.size());
    map.keySet().forEach(System.out::println);
  }
}
