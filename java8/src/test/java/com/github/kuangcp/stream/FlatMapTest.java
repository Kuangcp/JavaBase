package com.github.kuangcp.stream;

import com.github.kuangcp.time.GetRunTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * @author kuangchengping@qipeipu.com
 * @since 2019-07-05 11:07
 */
public class FlatMapTest {

  /**
   * test flatmap
   */
  @Test
  public void testFlatMap() {
    GetRunTime runTime = new GetRunTime();
    Map<String, List<String>> map = new HashMap<>();
    for (int i = 0; i < 10000; i++) {
      map.put(i + "", Arrays.asList("1", "2", "3", i + ""));
    }

    runTime.startCount();
    List<String> result = map.entrySet().stream()
        .flatMap(v -> v.getValue().stream())
        .collect(Collectors.toList());
    runTime.endCountOneLine();

    List<String> results = new ArrayList<>();

    runTime.startCount();
    map.forEach((k, v) -> results.addAll(v));
    runTime.endCountOneLine();
  }
}
