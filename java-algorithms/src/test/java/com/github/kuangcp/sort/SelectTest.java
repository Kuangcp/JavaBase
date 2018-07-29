package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class SelectTest {

  @Test
  public void testSort() {
    Select.sort(InitBigData.getData("select"));
  }
}
