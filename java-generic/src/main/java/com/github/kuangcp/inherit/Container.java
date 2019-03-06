package com.github.kuangcp.inherit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuangcp on 3/6/19-3:14 PM
 */
class Container<T> {

  private List<T> data = new ArrayList<>();

  T get(int index) {
    return data.get(index);
  }

  void add(T t) {
    data.add(t);
  }

  // Arrays.asList() as same
  static <B> Container<B> init(B b) {
    Container<B> container = new Container<>();
    container.add(b);
    return container;
  }
}
