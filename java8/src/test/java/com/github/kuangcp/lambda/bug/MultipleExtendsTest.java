package com.github.kuangcp.lambda.bug;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-11-28 19:43
 */
public class MultipleExtendsTest {

  @Test
  public void testMap() {
    List<C> list = Arrays.asList(new CImpl(), new CImpl());
    List<String> names = list.stream().map(A::getName).collect(Collectors.toList());
    System.out.println(names);
  }

  private static <E extends A & B> void getNames(List<E> list) {
    list.stream().map(E::getName).count();
    list.stream().map(E::getNum).count();
  }

  // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8142476
  @Test
  public void testMapWithGeneric() {
    List<C> list = Arrays.asList(new CImpl(), new CImpl());
    getNames(list);
    getNames(Arrays.asList(new D(), new D()));
  }
}


interface A {

  String getName();
}

interface B {

  Integer getNum();
}

interface C extends A, B {

}

class CImpl implements C {

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Integer getNum() {
    return null;
  }
}

class D implements A, B {

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Integer getNum() {
    return null;
  }
}