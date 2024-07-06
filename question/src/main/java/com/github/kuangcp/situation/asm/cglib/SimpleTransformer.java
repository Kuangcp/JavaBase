package com.github.kuangcp.situation.asm.cglib;

import net.sf.cglib.beans.BeanCopier;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-06-13 09:12
 */
class SimpleTransformer {

  class A {

  }

  class B {

  }

  private final BeanCopier COPIER = BeanCopier.create(A.class, B.class, false);

  public static final SimpleTransformer INSTANCE = new SimpleTransformer();

  private SimpleTransformer() {
  }
}
