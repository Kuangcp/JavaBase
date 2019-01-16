package com.github.kuangcp.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class CurrentModificationTest {

  // 使用HashMap 并发地发生修改(新增,删除)和读操作就会引发 ConcurrentModificationException
  // 使用 ConcurrentHashMap 就不会
  // TODO 为什么
  @Test
  public void testHashMap() {
    Map<String, String> map = new HashMap<>();

    testReadAndModifyMapByForEach(map);

    testReadAndModifyMapByIterator(map);
  }

  @Test
  public void testConcurrentHashMap() {
    Map<String, String> map = new ConcurrentHashMap<>();

    testReadAndModifyMapByForEach(map);

    testReadAndModifyMapByIterator(map);
  }

  private void testReadAndModifyMapByForEach(Map<String, String> map) {
    for (int i = 0; i < 4; i++) {
      map.put("key " + i, "value " + i);
    }

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

  private void testReadAndModifyMapByIterator(Map<String, String> map) {
    for (int i = 0; i < 4; i++) {
      map.put("key " + i, "value " + i);
    }

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
