package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;
import java.util.function.Consumer;

/**
 * 单参数单方法的JDK8内置接口
 * @author kuangcp on 18-11-22-下午6:15
 */
public class TranslateByOne8 implements Consumer<Point> {

  @Override
  public void accept(Point point) {
    point.translate(1, 1);
  }
}
