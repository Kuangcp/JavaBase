package com.github.kuangcp.sort;

/**
 * 快速排序
 * 最坏的情况：当数列是一样的数据 O(n^2)
 * 理想：O(n*log n)
 *
 * @author Myth
 */
public enum Quick implements SortAlgorithm {

  INSTANCE;

  @Override
  public void sort(int[] data) {
    QuickSort sort = new FirstImpl();
    sort.sortData(data, 0, data.length - 1);
  }


  interface QuickSort {

    void sortData(int[] data, int low, int high);
  }

  /**
   * 高效的写法
   */
  static class FirstImpl implements QuickSort {

    @Override
    public void sortData(int[] data, int low, int high) {
      if (low >= high) {
        return;
      }

      int lowIndex = low;
      int highIndex = high;

      int value = data[low];
      while (lowIndex < highIndex) {
        // 找出右边小于低位所在的标识值
        while (lowIndex < highIndex && data[highIndex] >= value) {
          highIndex -= 1;
        }
        data[lowIndex] = data[highIndex];

        // 找出左边大于标识值
        while (lowIndex < highIndex && data[lowIndex] <= value) {
          lowIndex += 1;
        }
        data[highIndex] = data[lowIndex];
      }

      data[lowIndex] = value;

      sortData(data, low, lowIndex - 1);
      sortData(data, highIndex + 1, high);
    }
  }

  /**
   * 个人手写
   */
  static class SecondImpl implements QuickSort {

    @Override
    public void sortData(int[] data, int low, int high) {
      int lowIndex = low;
      int highIndex = high;
      int index = data[low];

      while (lowIndex < highIndex) {
        while (lowIndex < highIndex && data[highIndex] >= index) {
          highIndex--;
        }
        if (lowIndex < highIndex) {
          int temp = data[highIndex];
          data[highIndex] = data[lowIndex];
          data[lowIndex] = temp;
          lowIndex++;
        }

        while (lowIndex < highIndex && data[lowIndex] <= index) {
          lowIndex++;
        }
        if (lowIndex < highIndex) {
          int temp = data[highIndex];
          data[highIndex] = data[lowIndex];
          data[lowIndex] = temp;
          highIndex--;
        }
      }

      if (lowIndex > low) {
        sortData(data, low, lowIndex - 1);
      }
      if (highIndex < high) {
        sortData(data, lowIndex + 1, high);
      }
    }
  }

  /**
   * 对象数组排序
   */
  public <T extends Comparable<? super T>> T[] quickSort(T[] data, int start, int end) {
    int low = start + 1, high = end;
    T key = data[start];

    if (start >= end) {
      return (data);
    }

    while (true) {
      while (data[high].compareTo(key) > 0) {
        high--;
      }
      while (data[low].compareTo(key) < 0 && low < high) {
        low++;
      }
      if (low >= high) {
        break;
      }
      if (data[low] == key) {
        high--;
      } else {
        low++;
      }
    }

    if (start < low - 1) {
      this.quickSort(data, start, low - 1);
    }
    if (high + 1 < end) {
      this.quickSort(data, high + 1, end);
    }

    return data;
  }
}