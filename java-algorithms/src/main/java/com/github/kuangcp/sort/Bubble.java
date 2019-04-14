package com.github.kuangcp.sort;

/**
 * 冒泡排序，从小到大
 * 最坏的情况：O(n^2)
 *
 * @author kcp
 */
public enum Bubble implements SortAlgorithm {

  INSTANCE;

  public void sort(int[] arr) {

    for (int i = 1; i < arr.length; i++) {
      //用来冒泡的语句，0到已排序的部分
      for (int j = 0; j < arr.length - i; j++) {
        //大就交换，把最大的沉入最后
        if (arr[j] > arr[j + 1]) {
          int temp = arr[j + 1];
          arr[j + 1] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }
}
