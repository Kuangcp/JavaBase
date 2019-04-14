package com.github.kuangcp.sort;

/**
 * 选择排序，小到大
 * 原理是：第一个依次与后面所有元素进行比较，遇到比自己小的就交换直到最后，第二轮就拿第二个元素去依次比较
 * 最坏的情况是： 时间复杂度是O(n^2)
 */
public enum Select implements SortAlgorithm {

  INSTANCE;

  public void sort(int[] arr) {
    for (int i = 0; i < arr.length - 1; i++) {
      //这个循环就是把较小数堆叠在数组头，依次与后面比较再交换
      for (int j = i + 1; j < arr.length; j++) {
        if (arr[i] > arr[j]) {
          int temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }

}
