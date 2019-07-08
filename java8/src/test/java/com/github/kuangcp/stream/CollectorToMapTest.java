package com.github.kuangcp.stream;

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
 */
@Slf4j
public class CollectorToMapTest {

  /**
   * 如果没有声明 重复key的策略, 就会直接抛出异常
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test(expected = IllegalStateException.class)
  public void testDuplicated() {
    List<Phone> phones = generatePhones();

    phones.stream().collect(Collectors.toMap(Phone::getName, Phone::getPrice));
  }

  /**
   * 发生重复后, 后来的值覆盖前面的值, 如果 (v1, v2) -> v1 则是忽略后来的值
   */
  @Test
  public void testDuplicatedWithHandled() {
    Map<String, Integer> result = generatePhones().stream()
        .collect(Collectors.toMap(Phone::getName, Phone::getPrice, (v1, v2) -> v2));

    result.forEach((k, v) -> log.info("{} {}", k, v));
  }

  private List<Phone> generatePhones() {
    return Arrays.asList(
        Phone.builder().name("a").price(1).build(),
        Phone.builder().name("b").price(2).build(),
        Phone.builder().name("a").price(3).build()
    );
  }

  @Data
  @Builder
  static class Phone {

    String name;
    int price;
  }

}
