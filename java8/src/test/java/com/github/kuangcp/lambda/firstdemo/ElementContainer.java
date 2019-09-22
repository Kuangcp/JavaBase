package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 对所有元素 执行逻辑
 *
 * @author kuangcp on 18-11-22-下午6:13
 */
class ElementContainer<T extends Point> extends ArrayList<T> {

  void forEach(ElementAction pointAction) {
    for (T point : this) {
      pointAction.action(point);
    }
  }
}
