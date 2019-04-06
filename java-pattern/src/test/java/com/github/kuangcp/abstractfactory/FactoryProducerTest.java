package com.github.kuangcp.abstractfactory;

import com.github.kuangcp.abstractfactory.base.Color;
import com.github.kuangcp.abstractfactory.base.Shape;
import com.google.common.base.Preconditions;
import java.util.Optional;
import org.junit.Test;

/**
 * 隐藏内部实现
 * @author kuangcp on 2019-04-07 12:42 AM
 */
public class FactoryProducerTest {

  @Test
  public void testMain() {
    Optional<AbstractFactory> shapeFactoryOpt = FactoryProducer.getFactory("SHAPE");
    Preconditions.checkArgument(shapeFactoryOpt.isPresent());

    Optional<Shape> shapeOpt = shapeFactoryOpt.get().getShape("RECTANGLE");
    Preconditions.checkArgument(shapeOpt.isPresent());
    shapeOpt.get().draw();

    Optional<AbstractFactory> colorFactoryOpt = FactoryProducer.getFactory("COLOR");
    Preconditions.checkArgument(colorFactoryOpt.isPresent());
    Optional<Color> colorOpt = colorFactoryOpt.get().getColor("RED");
    Preconditions.checkArgument(colorOpt.isPresent());
    colorOpt.get().fill();
  }
}
