package com.github.kuangcp.sort;

/**
 * 箱排序，有点不太好用，
 *
 * @author Myth
 */
public enum  Box implements SortAlgorithm {

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
