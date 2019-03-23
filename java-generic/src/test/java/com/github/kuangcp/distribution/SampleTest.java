package com.github.kuangcp.distribution;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 3/23/19-4:09 PM
 */
public class SampleTest {

  @Test
  public void testSample() {
    List<DogRef> list = IntStream.rangeClosed(1, 10).mapToObj(i -> new DogRef(i + "", i))
        .collect(Collectors.toList());

    List<DogRef> result = SampleUtil.sampleToSize(list, 2, DogRef.class);
    result.forEach(System.out::println);
  }
}
