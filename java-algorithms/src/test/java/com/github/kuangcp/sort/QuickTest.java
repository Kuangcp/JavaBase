package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class QuickTest {

  @Test
  public void testSort() {
    Quick.sort(InitBigData.getData("quick"), 0, InitBigData.AMOUNT - 1);
  }
}