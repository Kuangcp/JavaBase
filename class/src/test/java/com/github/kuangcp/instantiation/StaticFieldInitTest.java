package com.github.kuangcp.instantiation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-15 09:11
 */
@Slf4j
public class StaticFieldInitTest {

  @Test
  public void testInitStaticFieldSort() {
    assertThat(StaticFieldInit.num, equalTo(2));
    assertThat(StaticFieldInit.count, equalTo(1));
  }

  @Test
  public void testInitStaticField() {
    System.out.println("init class?");
    System.out.println(InitError.class);

    try {
      System.out.println("init class");
      System.out.println(InitError.num);
      Assert.fail();
    } catch (Error e) {
      log.error("", e);
    }
  }
}

class InitError {

  static int num = 0;
  static int version = initVersion();

  static int initVersion() {
    return 3 / 0;
  }
}