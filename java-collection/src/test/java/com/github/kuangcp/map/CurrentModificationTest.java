package com.github.kuangcp.map;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class CurrentModificationTest {


  @Test
  public void testLoop() {
    // 如何 判断 5个数中有四个数一样
//    int size = Stream.of(1, 3, 4, 4, 4).collect(Collectors.groupingBy(Integer::intValue), count());
//    System.out.println(size);
//    assert size != 2;
  }

  // 使用HashMap 并发地发生修改(新增,删除)和读操作就会引发 ConcurrentModificationException
  // 使用 ConcurrentHashMap 就不会
  // TODO 为什么
  // TODO 使用了ConcurrentHash 后避免了异常, 但是每次迭代的, 是最新的数据么
  @Test(expected = ConcurrentModificationException.class)
  public void testHashMap() {
    Map<String, String> map = new HashMap<>();

    log.info("foreach");
    testReadAndModifyMapByForEach(map);

    log.info("lambda");
    testReadAndModifyMapByLambda(map);
  }

  @Test
  public void testConcurrentHashMap() {
    Map<String, String> map = new ConcurrentHashMap<>();

    log.info("foreach");
    testReadAndModifyMapByForEach(map);

    log.info("lambda");
    testReadAndModifyMapByLambda(map);
  }

  private void testReadAndModifyMapByForEach(Map<String, String> map) {
    new Thread(() -> addItemToMap(map)).start();

    for (int i = 0; i < 100; i++) {
      log.info("read : i={}", i);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (String value : map.values()) {
      }
    }
  }

  private void testReadAndModifyMapByLambda(Map<String, String> map) {
    new Thread(() -> addItemToMap(map)).start();

    for (int i = 0; i < 100; i++) {
      log.info("read : i={}", i);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      map.values().forEach(value -> {
      });
    }
  }

  private void addItemToMap(Map<String, String> map) {
    for (int i = 0; i < 100; i++) {
      log.info("addItemToMap: i={}", i);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      map.put("t " + i, "1");
    }
  }
}
