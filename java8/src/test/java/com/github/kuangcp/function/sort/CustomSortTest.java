package com.github.kuangcp.function.sort;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

/**
 * @author kuangcp on 3/8/19-2:25 PM
 */
public class CustomSortTest {

  @Test
  public void testString() {
    List<String> list = Arrays.asList("map1", "ma2p3", "map23");
    Optional<String> maxOpt = list.stream().max((a, b) -> {
      String[] mapA = a.split("map");
      String[] mapB = b.split("map");
      if (mapA.length > 1 && mapB.length > 1) {
        return Integer.compare(Integer.parseInt(mapA[1]), Integer.parseInt(mapB[1]));
      }
      return 0;
    });

    assertThat(maxOpt.get(), equalTo("map23"));
  }
}
