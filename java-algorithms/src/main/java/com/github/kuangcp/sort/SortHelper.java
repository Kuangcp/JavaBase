package com.github.kuangcp.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

  static Map<SortAlgorithm, Integer> algorithms = new HashMap<>();

  static ArrayList<int[]> data = new ArrayList<>();

  static void init() {
    Radix.maxLen = String.valueOf(SCOPE).length();

    int count = -1;
    algorithms.put(Radix.INSTANCE, ++count);
    algorithms.put(Bubble.INSTANCE, ++count);
    algorithms.put(Insert.INSTANCE, ++count);
    algorithms.put(Select.INSTANCE, ++count);
    algorithms.put(Shell.INSTANCE, ++count);
    algorithms.put(Quick.INSTANCE, ++count);

    for (int i = 0; i < algorithms.size(); i++) {
      int[] temp = new int[AMOUNT];
      data.add(temp);
    }
    for (int i = 0; i < AMOUNT; i++) {
      int temp = ThreadLocalRandom.current().nextInt(SCOPE);
      for (int j = 0; j < algorithms.size(); j++) {
        data.get(j)[i] = temp;
      }
    }
  }

  static void showData(int[] data) {
    if (!show) {
      return;
    }

    for (int i = 0; i < data.length; i++) {
      int length = String.valueOf(SCOPE).length();
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