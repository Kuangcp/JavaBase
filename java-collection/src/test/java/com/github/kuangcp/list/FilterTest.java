package com.github.kuangcp.list;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-9-17-下午2:43
 */
@Slf4j
public class FilterTest {


  private List<String> list = new LinkedList<>();

  //  @Before
  public void initList() {
    for (int i = 0; i < 10; i++) {
      list.add("" + i);
    }
  }

  @Test
  public void testFilter() {
    List<String> result = list.stream().filter(String::isEmpty).collect(Collectors.toList());

    log.info("re: result={} {}", result, result.isEmpty());
  }
}
