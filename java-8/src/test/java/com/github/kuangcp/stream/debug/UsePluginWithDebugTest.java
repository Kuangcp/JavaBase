package com.github.kuangcp.stream.debug;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author kuangcp on 3/13/19-9:00 PM
 * plugin: Java Stream Debugger
 */
public class UsePluginWithDebugTest {

  @Test
  public void testFirst() {
    Stream.of("A", "B", 1, 2, null).filter(Objects::nonNull).forEach(System.out::println);
  }

  @Test
  public void testSecond() {
    List<Byte> result = Stream.of("1", "3", "8", "10", "22").map(Integer::parseInt)
        .filter(s -> s > 9).map(Integer::byteValue).collect(Collectors.toList());

    result.forEach(System.out::println);
  }
}
