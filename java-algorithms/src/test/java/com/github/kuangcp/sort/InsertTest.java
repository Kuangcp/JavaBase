package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class InsertTest {

  @Test
  public void testSort() throws Exception {
    Insert.sort(InitBigData.getData("insert"));
  }
}
