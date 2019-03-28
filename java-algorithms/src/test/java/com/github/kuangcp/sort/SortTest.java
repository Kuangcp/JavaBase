package com.github.kuangcp.sort;

import com.github.kuangcp.time.GetRunTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://github.com/jsonzou/jmockdata
 *
 * @author kuangcp on 3/28/19-7:42 PM
 */
@Slf4j
public class SortTest {

  @Test
  public void testSortCorrect() {

  }

  @Test
  public void testSortPerformance() {
    SortHelper.AMOUNT = 30000;

    SortHelper.init();

    SortHelper.algorithms.forEach((k, v) -> {
      log.info("sort: name={}", k.getName());
      System.out.println();

      int[] data = SortHelper.data.get(v);
      SortHelper.showData(data);

      GetRunTime runTime = new GetRunTime().startCount();
      k.sort(data);
      runTime.endCount(k.getName());

      SortHelper.showData(data);
    });
  }
}
