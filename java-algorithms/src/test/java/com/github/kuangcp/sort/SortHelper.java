package com.github.kuangcp.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by https://github.com/kuangcp
 * 用来初始化大量数据用于排序
 *
 * @author kuangcp
 */
public class SortHelper {

  // 数量级
  public static int AMOUNT = 1000;

  // 数据范围
  public static int SCOPE = 999999;
  public static boolean show = false;

  public static Map<SortAlgorithm, Integer> algorithms = new HashMap<>();

  public static ArrayList<int[]> data = new ArrayList<>();

  public static void init() {
    int count = -1;
    algorithms.put(Box.INSTANCE, ++count);
    algorithms.put(Bubble.INSTANCE, ++count);
    algorithms.put(Insert.INSTANCE, ++count);
    algorithms.put(Select.INSTANCE, ++count);
    algorithms.put(Shell.INSTANCE, ++count);
//    algorithms.put("quick", 3);

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

  public static void showData(int[] data) {
    if (!show) {
      return;
    }

    for (int i = 0; i < data.length; i++) {
      System.out.printf("%6d ", data[i]);
      if ((i + 1) % 11 == 0) {
        System.out.println();
      }
    }
    System.out.println();
  }
}