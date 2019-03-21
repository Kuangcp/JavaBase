package com.github.kuangcp.wildcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kuangcp on 3/21/19-5:51 PM
 */
public class SampleUtil {

  public static <K> List<Unit<K>> toUnitList(List<? extends SampleAble<K>> data) {
    List<Unit<K>> result = new ArrayList<>(data.size());
    for (int i = 0; i < data.size(); i++) {
      SampleAble<K> sampleAble = data.get(i);
      Unit<K> refUnit = new Unit<>(i, sampleAble.getId(), sampleAble.getWeight());
      result.add(refUnit);
    }
    return result;
  }

  public static <K> Optional<EnumeratedIntegerDistribution> distributionFromRefList(
      List<? extends SampleAble<K>> data) {
    if (Objects.isNull(data) || data.isEmpty()) {
      return Optional.empty();
    }

    List<Unit<K>> refUnits = toUnitList(data);
    return distribution(refUnits);
  }

  // index -> ref, weight
  public static <K> Optional<EnumeratedIntegerDistribution> distribution(
      Collection<Unit<K>> data) {
    if (Objects.isNull(data) || data.isEmpty()) {
      return Optional.empty();
    }
    double sum = data.stream().mapToInt(Unit::getWeight).sum();

    List<Double> probList = data.stream()
        .map(Unit::getWeight).map(value -> value / sum)
        .collect(Collectors.toList());

    List<Integer> idList = data.stream().map(Unit::getIndex).collect(Collectors.toList());

    EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(
        idList.stream().mapToInt(Integer::intValue).toArray(),
        probList.stream().mapToDouble(Double::doubleValue).toArray()
    );

    return Optional.of(distribution);
  }

  public static <K> Map<Integer, Unit<K>> convertToMap(List<Unit<K>> data) {
    return data.stream().collect(Collectors.toMap(Unit::getIndex, value -> value));
  }

  /**
   * 不重复的sample
   */
  public static List<Integer> sampleToSize(EnumeratedIntegerDistribution distribution, int size) {
    Preconditions.checkNotNull(distribution, "distribution is null");
    Preconditions.checkArgument(size > 0, "size is inactive");

    int[] sampled = distribution.sample(size);
    Set<Integer> unique = Arrays.stream(sampled).boxed().collect(Collectors.toSet());
    while (unique.size() < size) {
      int[] again = distribution.sample(size);
      Set<Integer> collect = Arrays.stream(again).boxed().collect(Collectors.toSet());
      unique.addAll(collect);
      log.debug("sampleToSize...another sampling...current sample {}, target unique sample {}",
          collect, unique);
    }

    ArrayList<Integer> list = new ArrayList<>(unique);
    Preconditions.checkArgument(list.size() >= size);
    return Lists.partition(list, size).get(0);
  }

  public static Optional<Integer> sampleToSize(EnumeratedIntegerDistribution distribution) {
    return Optional.ofNullable(sampleToSize(distribution, 1).get(0));
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
  
  public static <K> List<? super SampleAble<K>> sample(List<? extends SampleAble<K>> list,
      int count) {
    if (Objects.isNull(list) || list.isEmpty()) {
      return new ArrayList<>();
    }
    Map<K, SampleAble<K>> originMap = list.stream()
        .collect(Collectors.toMap(SampleAble::getId, v -> v));

    List<Unit<K>> refUnits = toUnitList(list);
    Map<Integer, Unit<K>> map = SampleUtil.convertToMap(refUnits);
    Optional<EnumeratedIntegerDistribution> distributionOpt = SampleUtil.distribution(refUnits);
    if (!distributionOpt.isPresent()) {
      return new ArrayList<>();
    }

    List<Integer> indexes = sampleToSize(distributionOpt.get(), count);
    return indexes.stream().map(map::get).map(Unit::getId)
        .map(originMap::get).collect(Collectors.toList());
  }
}
