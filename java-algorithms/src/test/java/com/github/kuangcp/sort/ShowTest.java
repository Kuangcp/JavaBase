package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 * 这个也证明了静态类在加载之前就已经在虚拟机中了, 所以能够在无需引用传递的情况下继续使用
 *
 * @author kuangcp
 */
public class ShowTest {

  @Test
  public void testShow() {
    for (int[] datum : InitBigData.data) {
      for (int i = 0; i < 30; i++) {
        System.out.print(datum[i] + " ");
      }
      System.out.println();
    }
  }
}
