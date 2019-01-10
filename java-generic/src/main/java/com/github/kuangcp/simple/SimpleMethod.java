package com.github.kuangcp.simple;

import java.io.Serializable;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:44
 * 泛型方法
 *
 * @author kuangcp
 */
public class SimpleMethod {

  /**
   * 简单的泛型方法 可以取代掉以往的Object的工具类的方法
   *
   * @param list 对象集合
   * @param <T> 类型必须是对象, 即使是基本数据类型,也要使用对应的包装类
   * @return 返回中间的对象
   */
  public static <T> T getMiddle(T[] list) {
    return list[list.length / 2];
  }

  /**
   * 传入一个数组,得到最大值
   *
   * @param list 数组
   * @param <T> 泛型约束  并且带有子类型约束,约束了一定要实现两个接口的类型才能正常使用这个方法
   * @return 返回最大值
   */
  public static <T extends Comparable & Serializable> T getMax(T[] list) {
    T target = list[list.length - 1];
    for (int i = list.length - 2; i > -1; i--) { // 这种写法就不会频繁的去获取list的长度
      if (target.compareTo(list[i]) < 0) {
        target = list[i];
      }
    }
    return target;
  }
}
