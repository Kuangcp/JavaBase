package com.github.kuangcp.situation.asm.cglib;

import net.sf.cglib.beans.BeanCopier;

/**
 * @author https://github.com/kuangcp on 2019-06-13 09:12
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
