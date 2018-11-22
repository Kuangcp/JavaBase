package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author kuangcp on 18-11-22-下午6:13
 */
public class PointArrayList extends ArrayList<Point> {

  void forEach(PointAction pointAction) {
    for (Point point : this) {
      pointAction.doForPoint(point);
    }
  }
}
