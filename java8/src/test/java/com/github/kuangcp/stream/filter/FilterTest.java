package com.github.kuangcp.stream.filter;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/6/19-5:08 PM
 */
@Slf4j
public class FilterTest {

  // 当改成 private 时, 才会在下面的方法警告在拆箱时可能导致NPE, Idea 2018 3.5
  public Boolean isTrue(int count) {
    if (count < 5) {
      return false;
    }
    if (count == 6) {
      return null;
    }
    return true;
  }

  @Test(expected = NullPointerException.class)
  public void testFilterWithNull() {
    long count = IntStream.rangeClosed(1, 10).filter(this::isTrue).count();
    System.out.println(count);
  }
}
