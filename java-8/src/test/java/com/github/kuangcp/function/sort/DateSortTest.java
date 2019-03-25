package com.github.kuangcp.function.sort;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-27-上午11:35
 */
public class DateSortTest {

  @Test
  public void testLocalDateTime() {
    List<LocalDateTime> datetimeList = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      LocalDateTime days = LocalDateTime.now().plusDays(i);
      datetimeList.add(days);
    }

    datetimeList.sort((a, b) -> {
      if (a.equals(b)) {
        return 0;
      }
      if (a.isAfter(b)) {
        return 1;
      } else {
        return -1;
      }
    });

    datetimeList.forEach(System.out::println);
  }

  @Test
  public void testSimple() {
    IntStream.rangeClosed(1, 10)
        .mapToObj(i -> LocalDateTime.now().plusDays(i)).sorted((a, b) -> a.compareTo(b) * -1)
        .forEach(System.out::println);

    List<LocalDateTime> data = IntStream.rangeClosed(1, 10)
        .mapToObj(i -> LocalDateTime.now().plusDays(i)).collect(Collectors.toList());
    data.sort((a, b) -> a.compareTo(b) * -1);
    data.forEach(System.out::println);
  }

  @Test
  public void testReversed() {
    IntStream.rangeClosed(1, 10).mapToObj(i -> LocalDateTime.now().plusDays(i))
        .sorted(Comparator.reverseOrder()).forEach(System.out::println);
  }
}
