package com.github.kuangcp.stream.collector;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-12 16:48
 */
public class CollectorTest {

  @Test
  public void testMergeSegment() {
    Optional<Integer> result = Stream.of(2, 4, 6, 3)
        .sorted(Comparator.comparing(Integer::intValue).reversed())
        .reduce((a, b) -> {
          System.out.println(a + " " + b);
          return a - b;
        });
    assertThat(result.get(), equalTo(-3));
  }
}
