package com.github.kuangcp.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基数排序 箱排序
 *
 * @author Myth
 */
public enum Radix implements SortAlgorithm {

  INSTANCE;

  //需要确定盒子的轮数 也就是数值的最大位数
  static int maxLen = 5;

  public void sort(int[] data) {
    List<Integer> temp = Arrays.stream(data).boxed().collect(Collectors.toList());
    // 0 -> []. 1 -> [], ... 9 -> []
    Map<Integer, List<Integer>> box = new HashMap<>();

    for (int i = 0; i < maxLen; i++) {
      box.clear();
      for (int value : temp) {
        int weight = (int) ((value / Math.pow(10, i)) % 10);
        addValue(box, weight, value);
      }

      temp = new ArrayList<>(data.length);
      for (int j = 0; j < 10; j++) {
        List<Integer> list = box.get(j);
        if (Objects.nonNull(list)) {
          temp.addAll(list);
        }
      }
    }

    setArray(temp, data);
  }

  private static void addValue(Map<Integer, List<Integer>> box, int weight, int value) {
    List<Integer> result = box.get(weight);
    if (Objects.isNull(result)) {
      result = new LinkedList<>();
      box.put(weight, result);
    }

    result.add(value);
  }

  private static void setArray(List<Integer> result, int[] data) {
    for (int i = 0; i < data.length; i++) {
      data[i] = result.get(i);
    }
  }
}
