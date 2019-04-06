package com.github.kuangcp.abstractfactory;

import java.util.Optional;

/**
 * @author kuangcp on 2019-04-07 12:40 AM
 */
public class FactoryProducer {

  public static Optional<AbstractFactory> getFactory(String choice) {
    if (choice.equalsIgnoreCase("SHAPE")) {
      return Optional.of(new ShapeFactory());
    } else if (choice.equalsIgnoreCase("COLOR")) {
      return Optional.of(new ColorFactory());
    }

    return Optional.empty();
  }
}
