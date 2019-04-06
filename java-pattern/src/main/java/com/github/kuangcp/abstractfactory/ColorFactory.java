package com.github.kuangcp.abstractfactory;

import com.github.kuangcp.abstractfactory.base.Color;
import com.github.kuangcp.abstractfactory.base.Shape;
import com.github.kuangcp.abstractfactory.domain.Green;
import com.github.kuangcp.abstractfactory.domain.Red;
import java.util.Objects;
import java.util.Optional;

/**
 * @author kuangcp on 2019-04-07 12:35 AM
 */
public class ColorFactory extends AbstractFactory {

  @Override
  public Optional<Color> getColor(String color) {
    if (Objects.isNull(color)) {
      return Optional.empty();
    }

    if (color.equalsIgnoreCase("RED")) {
      return Optional.of(new Red());
    } else if (color.equalsIgnoreCase("GREEN")) {
      return Optional.of(new Green());
    }
    return Optional.empty();
  }

  @Override
  public Optional<Shape> getShape(String shape) {
    return Optional.empty();
  }
}
