package com.github.kuangcp.stream.map;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-25 19:22
 */
@Slf4j
public class MapTest {


  @Test
  public void testConvertMap() {
    Map<Long, A> input = new HashMap<>();
    input.put(1L, new A());
    Map<Long, B> result = convert(input, B.class);
    log.info("result={}", result);
  }

  <I, R> R convert(I input, Class<R> target) {
    try {
      return target.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  <K, I, R> Map<K, R> convert(Map<K, I> inputMap, Class<R> target) {
    return inputMap.entrySet().stream().collect(Collectors
        .toMap(Entry::getKey, v -> convert(v.getKey(), target), (front, current) -> current));
  }
}

@Data
class A {

  private String name;
}

@Data
class B {

  private String name;
}