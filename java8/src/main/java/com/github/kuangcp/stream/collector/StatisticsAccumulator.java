package com.github.kuangcp.stream.collector;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

class StatisticsAccumulator {
        LocalDateTime maxTime;
        int sumValue;
        long count;
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;

        StatisticsAccumulator combine(StatisticsAccumulator other) {
            StatisticsAccumulator result = new StatisticsAccumulator();
            result.maxTime = Stream.of(this.maxTime, other.maxTime)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            result.sumValue = this.sumValue + other.sumValue;
            result.count = this.count + other.count;
            result.maxValue = Math.max(this.maxValue, other.maxValue);
            result.minValue = Math.min(this.minValue, other.minValue);
            return result;
        }

        Statistics toStatistics() {
            Statistics stats = new Statistics();
            stats.setMaxTime(Optional.ofNullable(maxTime));
            stats.setAvgValue(count > 0 ? (double) sumValue / count : 0.0);
            stats.setSumValue(sumValue);
            stats.setCount(count);
            stats.setMaxValue(maxValue != Integer.MIN_VALUE ? Optional.of(maxValue) : Optional.empty());
            stats.setMinValue(minValue != Integer.MAX_VALUE ? Optional.of(minValue) : Optional.empty());
            return stats;
        }
    }