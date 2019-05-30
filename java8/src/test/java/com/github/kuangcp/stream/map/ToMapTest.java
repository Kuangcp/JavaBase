package com.github.kuangcp.stream.map;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-13 10:34
 */
@Slf4j
public class ToMapTest {

  @Data
  @Builder
  static class Phone {

    String name;
    int price;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test(expected = IllegalStateException.class)
  public void testDuplicated() {
    List<Phone> phones = getPhones();

    phones.stream().collect(Collectors.toMap(Phone::getName, Phone::getPrice));
  }

  @Test
  public void testDuplicatedWithHandled() {
    List<Phone> phones = getPhones();

    // 发生重复后, 后来的值覆盖前面的值, 如果 (v1, v2) -> v1 则是忽略后来的值
    Map<String, Integer> result = phones.stream()
        .collect(Collectors.toMap(Phone::getName, Phone::getPrice, (v1, v2) -> v2));
    result.forEach((k, v) -> log.info("{} {}", k, v));
  }

  private List<Phone> getPhones() {
    return Arrays.asList(Phone.builder().name("a").price(1).build(),
        Phone.builder().name("b").price(2).build(),
        Phone.builder().name("a").price(3).build());
  }
}
