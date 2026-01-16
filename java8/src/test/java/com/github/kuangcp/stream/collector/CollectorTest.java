package com.github.kuangcp.stream.collector;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-12-12 16:48
 */
@Slf4j
public class CollectorTest {

    @Test
    public void testMergeSegment() {
        Optional<Integer> result = Stream.of(2, 4, 6, 3)
                .sorted(Comparator.comparing(Integer::intValue).reversed())
                .reduce((a, b) -> {
                    System.out.println(a + " " + b);
                    return a - b;
                });
        assertThat(result.get(), equalTo(-3));
    }

    @Test
    public void testCombine() throws Exception {
        List<DataPoint> dataPoints = new ArrayList<>();

//        List<DataPoint> dataPoints = Arrays.asList(
//                new DataPoint(LocalDateTime.of(2023, 1, 1, 10, 0), 100),
//                new DataPoint(LocalDateTime.of(2023, 1, 1, 11, 0), 200),
//                new DataPoint(LocalDateTime.of(2023, 1, 1, 12, 0), 150)
//        );
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 3000; i++) {
            LocalDateTime timestamp = now.plusDays(ThreadLocalRandom.current().nextInt(3000));
            DataPoint p = new DataPoint(timestamp, ThreadLocalRandom.current().nextInt(5000));
            dataPoints.add(p);
        }

        Statistics stats = dataPoints.stream()
                .reduce(new StatisticsAccumulator(),
                        (acc, point) -> {
                            acc.maxTime = Stream.of(acc.maxTime, point.getTimestamp())
                                    .filter(Objects::nonNull)
                                    .max(LocalDateTime::compareTo)
                                    .orElse(null);
                            acc.sumValue += point.getValue();
                            acc.count++;
                            acc.maxValue = Math.max(acc.maxValue, point.getValue());
                            acc.minValue = Math.min(acc.minValue, point.getValue());
                            return acc;
                        },
                        StatisticsAccumulator::combine
                )
                .toStatistics();
        log.info("stats={}", stats);
    }
}
