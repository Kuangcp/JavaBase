package com.github.kuangcp.forkjoin

import java.util.concurrent.RecursiveAction

/**
 *
 * @author kuangcp on 18-11-22-下午6:00
 */
// 继承这个类才可以看成是一个task
class ElementSorter extends RecursiveAction {

  // 这是一个标记量，当排序元素低于这个数量就用 Arrays.sort() 排序，大于就用归并
  private static final int SMALL_ENOUGH = 10
  private final Element[] elements
  private final int start, end
  private final Element[] result

  ElementSorter(Element[] elements) {
    this(elements, 0, elements.length)
  }

  ElementSorter(Element[] elements, int start, int end) {
    this.elements = elements
    this.start = start
    this.end = end
    this.result = new Element[elements.length]
  }
  // 合并排序
  void merge(ElementSorter left, ElementSorter right) {
    int i = 0
    int lCt = 0
    int rCt = 0
    while (lCt < left.size() && rCt < right.size()) {
      result[i++] = left.result[lCt] < right.result[rCt] ?
          left.result[lCt++] : right.result[rCt++]
    }
    while (lCt < left.size()) {
      result[i++] = left.result[lCt++]
    }
    while (rCt < right.size()) {
      result[i++] = right.result[rCt++]
    }
  }

  int size() {
    return end - start
  }

  Element[] getResult() {
    return result
  }
  // 父类声明的抽象方法
  @Override
  protected void compute() {
    if (size() < SMALL_ENOUGH) {
      System.arraycopy(elements, start, result, 0, size())
      Arrays.sort(result, 0, size())
    } else {
      int mid = (int) (size() / 2)
      ElementSorter left = new ElementSorter(elements, start, start + mid)
      ElementSorter right = new ElementSorter(elements, start + mid, end)
//            执行处，将两个task执行，这两个task的任务就是递归的拆解整个数组，然后进入下面的合并排序里
      invokeAll(left, right)
      // 调用会报错,因为没有没有对象（或者说 任务单元的资源）去调用这个方法，
      // StackOverflowError 这是啥，栈溢出
//            invoke()
      merge(left, right)

    }
  }
}
