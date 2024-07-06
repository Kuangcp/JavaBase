package com.github.kuangcp.strcuture.stackapp;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-12-29 23:45
 */
public class GetMinTest {


  @Test
  public void testA() throws Exception {
    GetMin app = new GetMin();
    app.pushA(2);
    app.pushA(0);
    app.pushA(6);
    app.pushA(10);
    app.pushA(1);
    app.pushA(0);
    app.popA();

    assertThat(app.getMin(), equalTo(0));
  }

  @Test
  public void testB() throws Exception {
    GetMin app = new GetMin();
    app.pushB(3);
    app.pushB(2);
    app.pushB(9);
    app.popB();
    assertThat(app.getMin(), equalTo(2));
  }
}
