package com.github.kuangcp.instantiation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-15 09:11
 */

public class StaticFieldInitTest {

  @Test
  public void test(){
    assertThat(StaticFieldInit.num , equalTo(2));
    assertThat(StaticFieldInit.count , equalTo(1));
  }
}
