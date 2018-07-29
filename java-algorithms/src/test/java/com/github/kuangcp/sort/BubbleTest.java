package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class BubbleTest {

  @Test
  public void testSort() {
    Bubble.sort(InitBigData.getData("bubble"));
  }
}