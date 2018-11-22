package com.github.kuangcp.stream.list;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-10-下午4:14
 */
public class HandleExceptionTest {

  private List<Integer> data = new ArrayList<>();

  @Before
  public void init() {
    for (int i = 0; i < 10; i++) {
      data.add(i);
    }
  }

  @Test
  public void testException() {
    try {
      data.forEach(s -> {
        System.out.println(s);
        if (s > 7) {
          throw new NullPointerException("Oops ");
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
