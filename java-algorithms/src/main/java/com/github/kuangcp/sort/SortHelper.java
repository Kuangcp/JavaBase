package com.github.kuangcp.sort;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp
 * 用来初始化大量数据用于排序
 *
 * @author kuangcp
 */
@Slf4j
class SortHelper {

  // 数量级
  static int AMOUNT = 1000;

  // 数据范围
  static int SCOPE = 999999;
  static boolean show = false;

  static List<SortAlgorithm> algorithms = Arrays.asList(
      Radix.INSTANCE
      , Bubble.INSTANCE
      , Insert.INSTANCE
      , Select.INSTANCE
      , Quick.INSTANCE
//      , Shell.INSTANCE
  );

  static int[] data;

  static void init() {
    Radix.maxLen = getScopeLength();

    data = new int[AMOUNT];
    for (int i = 0; i < AMOUNT; i++) {
      data[i] = ThreadLocalRandom.current().nextInt(SCOPE);
    }
  }

  private static int getScopeLength() {
    return String.valueOf(SCOPE).length();
  }

  static void showData(int[] data) {
    if (!show) {
      return;
    }

    for (int i = 0; i < data.length; i++) {
      int length = getScopeLength();
      System.out.printf("%" + length + "d ", data[i]);
      if ((i + 1) % 19 == 0) {
        System.out.println();
      }
    }
    System.out.println();
  }

  static void validate(int[] data) {
    for (int i = 1; i < data.length; i++) {
      if (data[i - 1] > data[i]) {
        log.error("sort algorithm has error");
        System.exit(1);
      }
    }
  }
}