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
    MainSortHelper.AMOUNT = 17;
    MainSortHelper.SCOPE = 999;
    MainSortHelper.show = true;

    MainSortHelper.init();

    MainSortHelper.algorithms.forEach((k, v) -> {
      log.info("sort: name={}", k.getName());

      int[] data = MainSortHelper.data.get(v);

      MainSortHelper.showData(data);
      k.sort(data);
      MainSortHelper.showData(data);

      MainSortHelper.validate(data);
      System.out.println();
    });
  }

  @Test
  public void testSortPerformance() {
    MainSortHelper.AMOUNT = 8000;
    MainSortHelper.SCOPE = 1000000;

    MainSortHelper.init();

    MainSortHelper.algorithms.forEach((k, v) -> {
      int[] data = MainSortHelper.data.get(v);

      GetRunTime runTime = new GetRunTime().startCount();
      k.sort(data);
      runTime.endCountOneLine(k.getName());

      MainSortHelper.validate(data);
      System.out.println();
    });
  }
}
