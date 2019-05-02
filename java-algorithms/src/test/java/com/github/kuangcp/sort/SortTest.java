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

  // test sort algorithms was correct
  @Test
  public void testSortCorrect() {
    SortHelper.AMOUNT = 170;
    SortHelper.SCOPE = 999;
    SortHelper.show = true;

    SortHelper.init();

    SortHelper.algorithms.forEach(sort -> {
      log.info("sort: name={}", sort.getName());
      int[] data = SortHelper.data;

      SortHelper.showData(data);
      int[] result = sort.sort(data);
      SortHelper.showData(result);

      SortHelper.validate(result);
      System.out.println();
    });
  }

  @Test
  public void testSortPerformance() {
    SortHelper.AMOUNT = 2000;
    SortHelper.SCOPE = 10;

    SortHelper.init();

    SortHelper.algorithms.forEach(sort -> {
      GetRunTime runTime = new GetRunTime().startCount();
      int[] result = sort.sort(SortHelper.data);
      runTime.endCountOneLine(sort.getName());

      SortHelper.validate(result);
    });
  }
}
