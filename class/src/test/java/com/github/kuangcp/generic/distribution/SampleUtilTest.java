package com.github.kuangcp.generic.distribution;

import com.github.kuangcp.time.GetRunTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

/**
 * @author kuangcp on 3/23/19-4:09 PM
 * 简单随机抽样原则上应是有放回的抽样，使用randsample(X,N,1)但大多数时候，人们常采用无放回的抽样，
 * 对应于randsample(X,N,0)，对于N<0.05 * lenght(X)的情况与有放回抽样的结果相比无太大差别
 */
public class SampleUtilTest {

  private GetRunTime run = new GetRunTime();

  @Before
  public void before() {
    run.startCount();
  }

  @After
  public void after() {
    run.endCount();
  }

  @Test
  public void testSample() {
    List<DogRef> list = IntStream.rangeClosed(1, 10).mapToObj(i -> new DogRef(i + "", i))
        .collect(Collectors.toList());

    List<DogRef> result = SampleUtil.sampleWithNoRepeated(list, 2);
    result.forEach(r -> {
      System.out.println(r);
      assertThat(r.getWeight(), lessThan(11));
    });

    assertThat(result.size(), equalTo(2));
  }

  @Test
  public void testBugSample() {
    List<DogRef> list = IntStream.rangeClosed(1, 2).mapToObj(i -> new DogRef(i + "", i))
        .collect(Collectors.toList());
    list.add(new DogRef("big", 1800120122));

    List<DogRef> result = SampleUtil.sampleWithNoRepeated(list, 2);

    result.forEach(System.out::println);
  }

  @Test
  public void testCorrectSample() {
    List<DogRef> list = IntStream.rangeClosed(1, 2).mapToObj(i -> new DogRef(i + "", (i + 1) * 10))
        .collect(Collectors.toList());
    list.add(new DogRef("big", 1800120122));

    List<DogRef> result = SampleUtil.sampleWithNoRepeated(list, 2);
    result.forEach(System.out::println);
  }

  @Test
  public void testSimple() {
    Map<DogRef, Integer> map = IntStream.rangeClosed(1, 2)
        .mapToObj(i -> new DogRef(i + "", (i + 1) * 10))
        .collect(Collectors.toMap(d -> d, DogRef::getWeight));

    map.put(new DogRef("big", 1800120122), 1800120122);

    List<DogRef> result = SampleUtil.sampleWithNoRepeated(map, 2);
    result.forEach(System.out::println);
  }

  @Test
  public void testCompare() {
    List<DogRef> list = IntStream.rangeClosed(1, 9)
        .mapToObj(i -> new DogRef(i + "", 10 * (i / 9 + 1))).collect(Collectors.toList());

//    list.forEach(System.out::println);
    int sum = 1000;
    int count = 0;

    GetRunTime run = new GetRunTime().startCount();
    for (int i = 0; i < sum; i++) {
      List<DogRef> dogRefs = SampleUtil.sampleWithNoRepeated(list, 4);
      boolean has = dogRefs.stream().anyMatch(dogRef -> dogRef.getWeight() == 20);
      if (has) {
        count++;
      }
    }
    run.endCountOneLine();

    System.out.println(count);

    count = 0;
    run.startCount();
    for (int i = 0; i < sum; i++) {
      list = IntStream.rangeClosed(1, 9)
          .mapToObj(j -> new DogRef(j + "", 10 * (j / 9 + 1))).collect(Collectors.toList());

      boolean has = SampleUtil.sampleWithNoRepeated(list, 4).stream()
          .anyMatch(dogRef -> dogRef.getWeight() == 20);
      if (has) {
        count++;
      }
    }
    run.endCountOneLine();
    System.out.println(count);
  }
}
