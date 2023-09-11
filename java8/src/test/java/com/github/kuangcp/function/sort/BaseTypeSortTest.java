package com.github.kuangcp.function.sort;


import com.github.kuangcp.time.GetRunTime;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kuangcp on 18-8-17-下午12:00
 * base data type use functional Comparator to sort
 * <p>
 * Double Float is the same way
 */
public class BaseTypeSortTest {

    @Test
    public void testInt() {
        List<Integer> lists = IntStream.rangeClosed(1, 10000)
                .map(i -> ThreadLocalRandom.current().nextInt(10000)).boxed()
                .collect(Collectors.toList());

        GetRunTime getRunTime = new GetRunTime().startCount();
        lists.sort(Comparator.comparingInt(Integer::intValue));
        getRunTime.endCount("int 排序完成");
    }

    @Test
    public void testLong() {
        List<Long> lists = IntStream.rangeClosed(1, 10000)
                .mapToLong(i -> ThreadLocalRandom.current().nextLong(10000)).boxed()
                .collect(Collectors.toList());

        GetRunTime getRunTime = new GetRunTime().startCount();
        lists.sort(Comparator.comparingLong(Long::longValue));
        getRunTime.endCount("long 排序完成");
    }
}
