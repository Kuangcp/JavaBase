package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;

/**
 * @author kuangcp on 18-11-22-下午6:14
 */
class TranslateByOne implements ElementAction {

  @Override
  public void action(Point point) {
    point.translate(1, 2);
  }
}