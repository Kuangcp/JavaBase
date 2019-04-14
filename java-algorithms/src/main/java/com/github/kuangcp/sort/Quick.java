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
    sortData(data, 0, data.length - 1);
  }

  /**
   * 高效的写法
   */
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

  public void sortData2(int[] data, int low, int high) {
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
      sortData2(data, low, lowIndex - 1);
    }
    if (highIndex < high) {
      sortData2(data, lowIndex + 1, high);
    }
  }

  public <T extends Comparable<? super T>> T[] quickSort(T[] targetArr,
      int start, int end) {
    int i = start + 1, j = end;
    T key = targetArr[start];
    // SortUtil<T>sUtil=newSortUtil<T>();

    if (start >= end) {
      return (targetArr);
    }
    /*
     * 从i++和j--两个方向搜索不满足条件的值并交换
     *
     * 条件为：i++方向小于key，j--方向大于key
     */
    while (true) {
      while (targetArr[j].compareTo(key) > 0) {
        j--;
      }
      while (targetArr[i].compareTo(key) < 0 && i < j) {
        i++;
      }
      if (i >= j) {
        break;
      }
      if (targetArr[i] == key) {
        j--;
      } else {
        i++;
      }
    }

    /* 关键数据放到‘中间’ */
    // sUtil.swap(targetArr,start,j);

    if (start < i - 1) {
      this.quickSort(targetArr, start, i - 1);
    }
    if (j + 1 < end) {
      this.quickSort(targetArr, j + 1, end);
    }

    return targetArr;
  }
}