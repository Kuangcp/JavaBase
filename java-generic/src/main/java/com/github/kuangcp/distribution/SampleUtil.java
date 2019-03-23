package com.github.kuangcp.distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

/**
 * @author kuangcp on 3/21/19-5:51 PM
 */
@Slf4j
public class SampleUtil {

  /**
   * 不重复的sample
   */
  public static List<Integer> sampleToSize(EnumeratedIntegerDistribution distribution, int size) {
    if (Objects.isNull(distribution) || size <= 0) {
      return new ArrayList<>();
    }

    Set<Integer> unique = new HashSet<>(size);
    while (unique.size() < size) {
      unique.add(distribution.sample());
    }

    return new ArrayList<>(unique);
  }

  public static Optional<Integer> sampleToSize(EnumeratedIntegerDistribution distribution) {
    return Optional.ofNullable(sampleToSize(distribution, 1).get(0));
  }

  public static <T extends SampleAble> List<T> sampleToSize(List<T> list, int count,
      Class<T> type) {
    return sampleResult(list, count, SampleUtil::sampleToSize, type);
  }

  /**
   * 允许重复
   */
  public static List<Integer> sampleToSizeRepeatable(EnumeratedIntegerDistribution distribution,
      int size) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(distribution.sample());
    }
    return result;
  }

  public static <T extends SampleAble> List<T> sampleToSizeRepeatable(List<T> list, int count,
      Class<T > type) {
    return sampleResult(list, count, SampleUtil::sampleToSizeRepeatable, type);
  }

  // TODO remove this warning
  @SuppressWarnings("unchecked")
  private static <T extends SampleAble> List<T> sampleResult(List<T> list, int count
      , BiFunction<EnumeratedIntegerDistribution, Integer, List<Integer>> function, Class<T> type) {
    if (Objects.isNull(list) || list.isEmpty()) {
      return new ArrayList<>();
    }

    Map<Integer, SampleAble> tempMap = new HashMap<>();
    IntStream.range(0, list.size()).forEach(i -> tempMap.put(i, list.get(i)));

    EnumeratedIntegerDistribution distribution = generateEnumerated(list, tempMap);

    List<Integer> indexes = function.apply(distribution, count);
    return indexes.stream().map(tempMap::get).map(v -> (T) v).collect(Collectors.toList());
  }

  private static EnumeratedIntegerDistribution generateEnumerated(List<? extends SampleAble> list,
      Map<Integer, SampleAble> tempMap) {
    double sum = list.stream().mapToInt(SampleAble::getWeight).sum();

    List<Double> probList = list.stream().map(SampleAble::getWeight).map(value -> value / sum)
        .collect(Collectors.toList());

    return new EnumeratedIntegerDistribution(
        tempMap.keySet().stream().mapToInt(Integer::intValue).toArray(),
        probList.stream().mapToDouble(Double::doubleValue).toArray()
    );
  }
}
