package com.github.kuangcp.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-01-08 16:41
 */
@Slf4j
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

    /**
     * @see java.util.stream.SliceOps#makeInt(java.util.stream.AbstractPipeline.AbstractPipeline, long, long)
     */
    @Test
    public void testLimitBlocked() throws Exception {

        // 从一个集合中随机选取一部分数据出来。 limit的目标值大于集合大小时，就会阻塞
        List<String> ids = Arrays.asList("1", "2", "3");

        // 有限集合产生的流不会阻塞
//        List<String> xl = ids.stream().limit(5).collect(Collectors.toList());
//        log.info("xl={}", xl);


        Stream.iterate(0, n -> n + 2).distinct().limit(10).forEach(System.out::println);
//        Random.RandomIntsSpliterator.tryAdvance

        int size = ids.size();
        List<String> pick = new Random()
                .ints(0, size)
//                .peek(v -> System.out.println(v))
                .distinct()
                .limit(5)
                .mapToObj(ids::get)
                .collect(Collectors.toList());
        log.info("pick={}", pick);


    }

    <T> boolean isFinite(Stream<T> stream) {
        return !Objects.equals(stream.spliterator().estimateSize(), Long.MAX_VALUE);
    }

    @Test
    public void testZip() throws Exception {

        Integer[] result = new Integer[5];
        Stream<Integer> zip = zip(Stream.of(1, 2, 4, 5).parallel(), Stream.of(2, 3, 10).parallel());
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
