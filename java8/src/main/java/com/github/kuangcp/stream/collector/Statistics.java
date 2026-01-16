package com.github.kuangcp.stream.collector;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class Statistics {
    private Optional<LocalDateTime> maxTime;
    private Double avgValue;
    private Integer sumValue;
    private Long count;
    private Optional<Integer> maxValue;
    private Optional<Integer> minValue;

    // 构造函数、getter、setter...

    public static Statistics combine(Statistics s1, Statistics s2) {
        Statistics result = new Statistics();
        result.maxTime = s1.maxTime
                .map(t1 -> s2.maxTime.filter(t2 -> !t1.isAfter(t2)).orElse(t1));
        result.sumValue = s1.sumValue + s2.sumValue;
        result.count = s1.count + s2.count;
        result.avgValue = result.count > 0 ? (double) result.sumValue / result.count : 0.0;
        result.maxValue = Stream.of(s1.maxValue, s2.maxValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Integer::compare);
        result.minValue = Stream.of(s1.minValue, s2.minValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Integer::compare);
        return result;
    }
}