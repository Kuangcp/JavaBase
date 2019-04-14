package com.github.kuangcp;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 泛型类中创建, 类型变量类型的数组,使用Array.newInstance()是推荐的方式
 * 那么create方法就能创建类型变量约束的数组, 初始化为空
 * 暂时想到的用途就是泛型约束的创建 一个数组出来
 */
public class ArrayMaker<T> {

  private Class<T> kind;

  private ArrayMaker(Class<T> kind) {
    this.kind = kind;
  }

  @SuppressWarnings("unchecked")
  private T[] create(int size) {
    return (T[]) Array.newInstance(kind, size);
  }

  public static void main(String[] args) {
    ArrayMaker<String> stringMaker = new ArrayMaker<>(String.class);
    String[] stringArray = stringMaker.create(9);
    System.out.println(Arrays.toString(stringArray));
  }
}
/* Output:
[null, null, null, null, null, null, null, null, null]
*/
