package com.github.kuangcp.sort;

import java.util.Arrays;

/**
 * 选择排序，小到大
 * 原理是：第一个依次与后面所有元素进行比较，遇到比自己小的就交换直到最后，第二轮就拿第二个元素去依次比较
 * 最坏的情况是： 时间复杂度是O(n^2)
 */
public enum Select implements SortAlgorithm {

  INSTANCE;

  public int[] sort(int[] data) {
    int[] result = Arrays.copyOf(data, data.length);

    for (int i = 0; i < result.length - 1; i++) {
      //这个循环就是把较小数堆叠在数组头，依次与后面比较再交换
      for (int j = i + 1; j < result.length; j++) {
        if (result[i] > result[j]) {
          int temp = result[i];
          result[i] = result[j];
          result[j] = temp;
        }
      }
    }
    return result;
  }

}
