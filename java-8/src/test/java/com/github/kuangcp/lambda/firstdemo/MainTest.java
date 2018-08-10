package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class MainTest {

  private PointArrayList pointArrayList = new PointArrayList();

  @Before
  public void init() {
    pointArrayList.add(new Point(1, 1));
    pointArrayList.add(new Point(5, 5));
  }

  @Test
  // 利用多态
  public void testByPolymorphic() {
    // 实现内部迭代, 将一个实现了对应接口的具体动作传入
    pointArrayList.forEach(new TranslateByOne());

    for (Point point : pointArrayList) {
      System.out.println("for each " + point.toString());
    }
  }

  @Test
  public void testConsumer() {
    // 使用java8的 Consumer 的accept方法 （针对于接口单方法的一个抽象接口）
    pointArrayList.forEach(new TranslateByOne8());
    for (Point point : pointArrayList) {
      System.out.println("Java8\t " + point.toString());
    }
  }

  @Test
  public void testAnonymousInnerClass() {
    // 就是将上面的 TranslateByOne8 以匿名内部类形式实现

    pointArrayList.forEach(new Consumer<Point>() {
      @Override
      public void accept(Point point) {
        point.translate(1, 1);
        System.out.println("AnonymousInnerClass " + point);
      }

    });

  }

  @Test
  public void testLambda() {
    //lambda 表达式
    pointArrayList.forEach((Consumer<Point>) point -> point.translate(1, 1));
    // 将lambda参数列表映射为假想的方法的参数列表
    // 裁剪了匿名内部类定义的额外代码，实现lambda
    for (Point point : pointArrayList) {
      System.out.println("lambda\t " + point.toString());
    }
  }

}

// TODO 这个类定义的意义
class PointArrayList extends ArrayList<Point> {

  void forEach(PointAction pointAction) {
    for (Point point : this) {
      pointAction.doForPoint(point);
    }
  }
}

// 自定义接口和方法
interface PointAction {

  void doForPoint(Point point);
}

class TranslateByOne implements PointAction {

  @Override
  public void doForPoint(Point point) {
    point.translate(1, 1);
  }
}

// 单参数单方法的JDK8内置接口
class TranslateByOne8 implements Consumer<Point> {

  @Override
  public void accept(Point point) {
    point.translate(1, 1);
  }
}

