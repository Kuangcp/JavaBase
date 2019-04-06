package com.github.kuangcp.abstractfactory.domain;

import com.github.kuangcp.abstractfactory.base.Color;

/**
 * @author kuangcp on 2019-04-07 12:33 AM
 */
public class Red implements Color {

  @Override
  public void fill() {
    System.out.println("Red::fill");
  }
}
