package com.github.kuangcp.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp
 * 用来初始化大量数据用于排序
 *
 * @author kuangcp
 */
public class InitBigData {

  // 排序的总方法
  private final static int SORT_NUM = 6;
  // 数量级
  public final static int AMOUNT = 50;
  // 数据范围
  private final static int SCOPE = 10000;


  private static Map<String, Integer> methodMap = new HashMap<>();
  public static ArrayList<int[]> data = new ArrayList<>();

  static {
    for (int i = 0; i < SORT_NUM; i++) {
      int[] temp = new int[AMOUNT];
      data.add(temp);
    }
    for (int i = 0; i < AMOUNT; i++) {
      int temp = (int) (Math.random() * SCOPE);
      for (int j = 0; j < SORT_NUM; j++) {
        data.get(j)[i] = temp;
      }
    }
    methodMap.put("box", 0);
    methodMap.put("bubble", 1);
    methodMap.put("insert", 2);
    methodMap.put("quick", 3);
    methodMap.put("select", 4);
    methodMap.put("shell", 5);
  }

  public static int[] getData(String type) {
    return data.get(methodMap.get(type));
  }
}
