package com.github.kuangcp.generic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author kuangcp on 2019-04-22 11:39 AM
 */
@Slf4j
public class ArrayUtilsTest {

  @Test
  public void testCreateArray() {
    String[] stringArray = ArrayUtils.create(String.class, 9);

    System.out.println(Arrays.toString(stringArray));

    assert Arrays.stream(stringArray).allMatch(Objects::isNull);
  }

  @Test
  public void testSort() {
    List<Integer> list = Arrays.asList(3, 2, 5);
    List<Integer> result = ArrayUtils.sort(list);

    log.info("result={}", result);
  }

  @Test
  public void testSortToArray() {
    List<Integer> list = Arrays.asList(3, 2, 5);
    // ArrayUtils.sortToArray(list) 实际返回的是 (Comparable[])  但是这里需要强转为 Integer[] 这个转型失败了
    Integer[] result = ArrayUtils.sortToArray(list);

    log.info("result={}", Arrays.toString(result));
  }
}
