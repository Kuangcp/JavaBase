package com.github.kuangcp.sort;

/**
 * 基数排序 箱排序
 *
 * @author Myth
 */
public enum Radix implements SortAlgorithm {

  INSTANCE;

  public void sort(int[] arr) {
    //盒子个数
    for (int i = 0; i < 1000; i++) {
      for (int anArr : arr) {
        if (anArr == i) {
          // TODO 放入链队列中
        }
      }
    }
  }

} 
