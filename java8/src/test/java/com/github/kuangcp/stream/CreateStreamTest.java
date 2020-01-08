package com.github.kuangcp.stream;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-01-08 16:41
 */
public class CreateStreamTest {

  @Test
  public void testOfWithNull(){
    List<String> list = Stream.of(null, "test", "name").filter(Objects::nonNull)
        .collect(Collectors.toList());
    System.out.println(list);
  }
}
