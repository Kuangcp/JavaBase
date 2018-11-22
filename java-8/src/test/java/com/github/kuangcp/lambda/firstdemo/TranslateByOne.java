package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;

/**
 * @author kuangcp on 18-11-22-下午6:14
 */
public class TranslateByOne implements PointAction {

  @Override
  public void doForPoint(Point point) {
    point.translate(1, 1);
  }
}