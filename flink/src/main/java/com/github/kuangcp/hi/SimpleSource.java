package com.github.kuangcp.hi;

import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp
 * 2019-06-16 15:12
 */
@Slf4j
class SimpleSource {

  private String id;

  private static final int pageNum = 8;
  private static final int pageSize = 20;

  private int cursor;

  SimpleSource(String id) {
    this.id = id;
  }

  boolean hasNextPage() {
    return cursor < pageNum;
  }

  List<String> generateResource() {
    cursor++;
    log.info("source: id={} cursor={}", id, cursor);
    return IntStream.rangeClosed(1, pageSize)
        .mapToObj(i -> Month.of((i + cursor) % 12 + 1).toString())
        .collect(Collectors.toList());
  }
}
