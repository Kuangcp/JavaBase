package com.github.kuangcp.sort;

/**
 * @author kuangcp on 3/28/19-7:29 PM
 */
public interface SortAlgorithm {

  void sort(int[] data);

  default String getName() {
    return this.getClass().getSimpleName();
  }
}
