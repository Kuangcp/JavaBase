package com.github.kuangcp.hi;

import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟数据库分页查询
 *
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> 2019-06-16 15:12
 */
@Slf4j
class SimpleSource {

  private String id;

  private static final int pageNum = 40;
  private static final int pageSize = 500;

  private int cursor;

  SimpleSource(String id) {
    this.id = id;
  }

  boolean hasNextPage() {
    return cursor < pageNum;
  }

  List<String> generateResource() {
    delayTime();
    cursor++;
    log.info("source: id={} cursor={}", id, cursor);
    return IntStream.rangeClosed(1, pageSize)
        .mapToObj(i -> Month.of((i + cursor) % 12 + 1).toString())
        .collect(Collectors.toList());
  }

  /**
   * 延长Flink执行时间
   */
  private void delayTime() {
    try {
      TimeUnit.MILLISECONDS.sleep(300);
    } catch (InterruptedException e) {
      log.error("", e);
    }
  }
}
