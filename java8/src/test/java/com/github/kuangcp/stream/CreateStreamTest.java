package com.github.kuangcp.stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-01-08 16:41
 */
public class CreateStreamTest {

  @Test
  public void testOfWithNull() {
    List<String> list = Stream.of(null, "test", "name").filter(Objects::nonNull)
        .collect(Collectors.toList());
    System.out.println(list);
  }

  /**
   * 判断是否无限流， 理论上是无法实现的，但是这里可以估计
   */
  @Test
  public void testJudgeFinite() {
    assertFalse(isFinite(Stream.iterate(1, x -> x)));
    assertTrue(isFinite(Stream.of(1)));
    assertTrue(isFinite(Stream.of(1, 2, 3, 4).limit(1)));
  }

  <T> boolean isFinite(Stream<T> stream) {
    return !Objects.equals(stream.spliterator().estimateSize(), Long.MAX_VALUE);
  }

  @Test
  public void testZip() throws Exception {

    Integer[] result = new Integer[5];
    Stream<Integer> zip = zip(Stream.of(1, 2, 4, 5).parallel(), Stream.of(2, 3,10).parallel());
    zip.collect(Collectors.toList()).toArray(result);

    List<Integer> excepts = Arrays.asList(1, 2, 2, 3, 4, 10, 5);
    Integer[] exceptArray = new Integer[5];
    excepts.toArray(exceptArray);
    assertArrayEquals(result, exceptArray);
  }

  <T> Stream<T> zip(Stream<T> left, Stream<T> right) {
    Iterator<T> leftIterator = left.iterator();
    Iterator<T> rightIterator = right.iterator();

    Iterator<T> iterator = new Iterator<T>() {
      private boolean left = false;

      @Override
      public boolean hasNext() {
        return leftIterator.hasNext() || rightIterator.hasNext();
      }

      @Override
      public T next() {
        left = !left;
        if (left && !leftIterator.hasNext()) {
          return rightIterator.next();
        }
        if (!left && !rightIterator.hasNext()) {
          return leftIterator.next();
        }
        return left
            ? leftIterator.next()
            : rightIterator.next();
      }
    };

    Iterable<T> iterable = () -> iterator;
    boolean parallel = left.isParallel() || right.isParallel();
    return StreamSupport.stream(iterable.spliterator(), parallel);
  }
}
