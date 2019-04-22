package com.github.kuangcp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArrayUtils {

  /**
   * 泛型类中创建, 类型变量类型的数组,使用Array.newInstance()是推荐的方式
   */
  static <T> T[] create(Class<T> target, int size) {
    return (T[]) Array.newInstance(target, size);
  }

  static <T extends Comparable<T>> List<T> sort(List<T> list) {
    return Arrays.asList(list.toArray((T[]) new Comparable[list.size()]));
  }

  // 字节码中看到的是返回值强转为 (Comparable[]) 实际上不能满足调用方的需求, 这个接口必然报错
  // TODO 为什么这么做 不强转为 T[] ?
  static <T extends Comparable<T>> T[] sortToArray(List<T> list) {
    return list.toArray((T[]) new Comparable[list.size()]);
  }
}
