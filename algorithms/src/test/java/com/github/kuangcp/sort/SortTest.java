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

    // https://github.com/Kuangcp/GoBase/tree/master/algorithm/sort
    // test sort algorithms was correct
    @Test
    public void testSortCorrect() {
        SortHelper.AMOUNT = 170;
        SortHelper.SCOPE = 999;
        SortHelper.show = true;

        SortHelper.init();

        SortHelper.algorithms.forEach((key, value) -> {
            log.info("sort: name={}", key);
            int[] data = SortHelper.data;

            SortHelper.showData(data);
            int[] result = value.sort(data);
            SortHelper.showData(result);

            SortHelper.validate(result);
            System.out.println();
        });
    }

    @Test
    public void testSingleSortCorrect() {
        SortHelper.AMOUNT = 170;
        SortHelper.SCOPE = 999;
        SortHelper.show = true;
        Sorter sort = Insert::sort;

        SortHelper.init();
        int[] data = SortHelper.data;

        SortHelper.showData(data);
        int[] result = sort.sort(data);
        SortHelper.showData(result);

        SortHelper.validate(result);
        System.out.println();
    }

    @Test
    public void testSortPerformance() {
        SortHelper.AMOUNT = 30000;
        SortHelper.SCOPE = 1000000;

        SortHelper.init();

        for (int i = 0; i < 10; i++) {
            SortHelper.algorithms.forEach((key, value) -> {
                GetRunTime runTime = new GetRunTime().startCount();
                int[] result = value.sort(SortHelper.data);
                runTime.endCountOneLine(key);

//            SortHelper.validate(result);
            });
            System.out.println();
        }
    }
}
