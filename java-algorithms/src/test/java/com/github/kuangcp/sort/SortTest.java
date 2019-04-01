package com.github.kuangcp.sort;

import com.github.kuangcp.time.GetRunTime;
import java.util.Arrays;
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
    SortHelper.AMOUNT = 17;
    SortHelper.SCOPE = 999;
    SortHelper.show = true;

    SortHelper.init();

    SortHelper.algorithms.forEach((k, v) -> {
      log.info("sort: name={}", k.getName());

      int[] data = SortHelper.data.get(v);

      SortHelper.showData(data);
      k.sort(data);
      SortHelper.showData(data);

      SortHelper.validate(data);
      System.out.println();
    });
  }

  @Test
  public void testSortPerformance() {
    SortHelper.AMOUNT = 8000;
    SortHelper.SCOPE = 1000000;

    SortHelper.init();

    SortHelper.algorithms.forEach((k, v) -> {
      int[] data = SortHelper.data.get(v);

      GetRunTime runTime = new GetRunTime().startCount();
      k.sort(data);
      runTime.endCountOneLine(k.getName());

      SortHelper.validate(data);
      System.out.println();
    });
  }
}
