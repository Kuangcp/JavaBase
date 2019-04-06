package com.github.kuangcp.abstractfactory.domain;

import com.github.kuangcp.abstractfactory.base.Shape;

/**
 * @author kuangcp on 2019-04-07 12:18 AM
 */
public class Rectangle implements Shape {

  @Override
  public void draw() {
    System.out.println("Rectangle::draw");
  }
}
