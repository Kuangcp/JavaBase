package com.github.kuangcp.stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.github.kuangcp.time.GetRunTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-07-05 11:07
 */
public class FlatMapTest {

  /**
   * test flatmap
   */
  @Test
  public void testFlatMap() {
    int amount = 1000000;
    GetRunTime runTime = new GetRunTime();
    Map<String, List<String>> map = new HashMap<>();

    for (int i = 0; i < amount; i++) {
      map.put(i + "", Arrays.asList("1", "2", "3", i + ""));
    }

    // flatmap
    runTime.startCount();
    List<String> result = map.entrySet().stream()
        .flatMap(v -> v.getValue().stream())
        .collect(Collectors.toList());
    runTime.endCountOneLine();
    assertThat(result.size(), equalTo(4 * amount));

    // forEach addAll
    List<String> results = new ArrayList<>();
    runTime.startCount();
    map.forEach((k, v) -> results.addAll(v));
    runTime.endCountOneLine();
    assertThat(results.size(), equalTo(4 * amount));
  }

  @Test
  public void testDouble() {
    double d = 23;
    Object ob = d;
    System.out.println(ob);
  }
}
