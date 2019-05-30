package com.github.kuangcp.abstractfactory;

import com.github.kuangcp.abstractfactory.base.Color;
import com.github.kuangcp.abstractfactory.base.Shape;
import com.github.kuangcp.abstractfactory.domain.Rectangle;
import com.github.kuangcp.abstractfactory.domain.Square;
import java.util.Objects;
import java.util.Optional;

/**
 * @author kuangcp on 2019-04-07 12:35 AM
 */
public class ShapeFactory extends AbstractFactory {

  @Override
  public Optional<Color> getColor(String color) {
    return Optional.empty();
  }

  @Override
  public Optional<Shape> getShape(String shape) {
    if (Objects.isNull(shape)) {
      return Optional.empty();
    }

    if (shape.equalsIgnoreCase("RECTANGLE")) {
      return Optional.of(new Rectangle());
    } else if (shape.equalsIgnoreCase("SQUARE")) {
      return Optional.of(new Square());
    }
    return Optional.empty();
  }
}
