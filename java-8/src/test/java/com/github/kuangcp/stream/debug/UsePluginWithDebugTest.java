package com.github.kuangcp.stream.debug;

import java.util.Objects;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author kuangcp on 3/13/19-9:00 PM
 */
public class UsePluginWithDebugTest {

  @Test
  public void testFirst() {
    Stream.of("A", "B", 1, 2, null)
        .filter(Objects::nonNull)
        .forEach(System.out::println);
  }
}
