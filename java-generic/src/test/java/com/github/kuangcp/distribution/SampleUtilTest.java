package com.github.kuangcp.distribution;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 3/23/19-4:09 PM
 */
public class SampleUtilTest {

  @Test
  public void testSample() {
    List<DogRef> list = IntStream.rangeClosed(1, 10).mapToObj(i -> new DogRef(i + "", i))
        .collect(Collectors.toList());

    List<DogRef> result = SampleUtil.sampleToSize(list, 2, DogRef.class);
    result.forEach(r -> {
      System.out.println(r);
      assertThat(r.getWeight(), lessThan(11));
    });

    assertThat(result.size(), equalTo(2));
  }

  @Test
  public void testCollector(){
    IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
  }
}
