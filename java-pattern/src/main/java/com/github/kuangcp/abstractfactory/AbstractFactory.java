package com.github.kuangcp.abstractfactory;

import com.github.kuangcp.abstractfactory.base.Color;
import com.github.kuangcp.abstractfactory.base.Shape;
import java.util.Optional;

/**
 * @author kuangcp on 2019-04-07 12:33 AM
 */
public abstract class AbstractFactory {

  public abstract Optional<Color> getColor(String color);

  public abstract Optional<Shape> getShape(String shape);
}
