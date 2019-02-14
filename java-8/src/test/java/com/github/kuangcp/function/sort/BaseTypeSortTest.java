package com.github.kuangcp.function.sort;


import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-17-下午12:00
 * base data type use functional Comparator to sort
 *
 * Double Float is the same way
 */
public class BaseTypeSortTest {

  @Test
  public void testInt() {
    List<Integer> lists = IntStream.rangeClosed(1, 10000)
        .map(i -> ThreadLocalRandom.current().nextInt(10000)).boxed()
        .collect(Collectors.toList());

    GET_RUN_TIME.startCount();
    lists.sort(Comparator.comparingInt(Integer::intValue));
    GET_RUN_TIME.endCount("排序完成");
  }

  // TODO 一起执行时 为什么这个后执行的方法要快, 单独执行就不会有这样的情况
  @Test
  public void testLong() {
    List<Long> lists = IntStream.rangeClosed(1, 10000)
        .mapToLong(i -> ThreadLocalRandom.current().nextLong(10000)).boxed()
        .collect(Collectors.toList());

    GET_RUN_TIME.startCount();
    lists.sort(Comparator.comparingLong(Long::longValue));
    GET_RUN_TIME.endCount("排序完成");
  }
}
