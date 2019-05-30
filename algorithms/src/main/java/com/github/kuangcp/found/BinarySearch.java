package com.github.kuangcp.found;

/**
 * TODO 当数据有重复的时候，这个算法并不能做到返回数据第一次出现的地方
 */
public class BinarySearch {
  private static int index = -2;

  private static void found(int[] arr, int left, int right, int data) {

    int mid = (left + right) / 2;
    if (arr[mid] == data) {
      index = mid;
    }

    if (arr[mid] > data && left < mid - 1) {
      found(arr, left, mid, data);
    }

    if (arr[mid] < data && right > mid + 1) {
      found(arr, mid, right, data);
    }
  }

  /**
   * find num from arr
   *
   * @return index not found then return -1
   */
  public int find(int[] arr, int data) {
    found(arr, 0, arr.length, data);
    return index + 1;
  }
}
