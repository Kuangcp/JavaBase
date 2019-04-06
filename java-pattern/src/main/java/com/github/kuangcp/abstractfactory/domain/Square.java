package com.github.kuangcp.abstractfactory.domain;

import com.github.kuangcp.abstractfactory.base.Shape;

/**
 * @author kuangcp on 2019-04-07 12:31 AM
 */
public class Square implements Shape {

  @Override
  public void draw() {
    System.out.println("Square::draw");
  }
}
