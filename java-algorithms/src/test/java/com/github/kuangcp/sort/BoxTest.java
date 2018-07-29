package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class BoxTest {

  @Test
  public void testSort() {
    Box.sort(InitBigData.getData("box"));
  }
}