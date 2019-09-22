package com.github.kuangcp.lambda.firstdemo;

import java.awt.Point;

/**
 * 一个接口只有一个方法，就已经是函数式接口了，注解仅标记作用
 * 等价于 Consumer<Point> 如 TranslateByOne8 的实现
 *
 * @author kuangcp on 18-11-22-下午6:14
 */
@FunctionalInterface
interface ElementAction {

  void action(Point point);
}
