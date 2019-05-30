package com.github.kuangcp.strcuture.stackapp;

import org.junit.Test;

/**
 * @author kuangcp on 3/28/19-9:01 PM
 */
public class ShowStackPopTest {

  @Test
  public void testMain() {
    ShowStackPop pops = new ShowStackPop();
    pops.init();
    pops.deal("");

    for (String line : pops.getResults()) {
      System.out.println(line);
    }
  }
}