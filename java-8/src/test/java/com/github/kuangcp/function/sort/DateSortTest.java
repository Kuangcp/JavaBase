package com.github.kuangcp.function.sort;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
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
}
