package com.github.kuangcp.simple;

import lombok.Data;

/**
 * Created by https://github.com/kuangcp on 18-1-10  下午10:06
 * 最为简单的一个泛型类的使用 类似于C++的模板类
 * 启发 https://mp.weixin.qq.com/s?__biz=MzAxOTc0NzExNg==&mid=2665514015&idx=1&sn=12409f705c6d266e4cd062e78ce50be0&chksm=80d67c5cb7a1f54a68ed83580b63b4acded0df525bb046166db2c00623a6bba0de3c5ad71884&scene=21#wechat_redirect
 * 运行的详情查看测试类
 *
 * @author kuangcp
 */
@Data
class Pair<T> {

  private T first;
  private T second;

  public Pair() {
    first = null;
    second = null;
  }

  public Pair(T first, T second) {
    this.first = first;
    this.second = second;
  }

  /**
   * 简单的泛型方法 可以取代掉以往的Object的工具类的方法
   */
  public static <T> T middle(T[] list) {
    return list[list.length / 2];
  }

  /**
   * 得到最小最大值
   *
   * @param list 值集合
   * @param <T> 类型约束
   * @return first 最小 second 最大
   */
  public static <T extends Comparable<T>> Pair<T> minAndMax(T[] list) {
    if (list == null || list.length == 0) {
      return null;
    }

    Pair<T> result = new Pair<>(list[0], list[0]);
    if (list.length == 1) {
      return result;
    }

    for (T element : list) {
      if (result.getFirst().compareTo(element) > 0) {
        result.setFirst(element);
      }
      if (result.getSecond().compareTo(element) < 0) {
        result.setSecond(element);
      }
    }
    return result;
  }
}
