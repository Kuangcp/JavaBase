package com.github.kuangcp.generic.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kuangcp on 3/21/19-5:51 PM
 * TODO 自定义 收集器 接口, 与流结合使用
 */
@Slf4j
public class SampleUtil {

  /**
   * 不重复的sample
   * 放回式取样 概率相差很大时可能循环很多次 这是有缺陷的
   */
  public static <T extends SampleAble> List<T> sampleWithNoRepeatedAndPutBack(List<T> list,
                                                                              int count) {
    return sampleResult(list, count, SampleUtil::sampleWithNoRepeated);
  }

  /**
   * 不重复的sample
   * 不放回 随机取样
   */
  public static <T extends SampleAble> List<T> sampleWithNoRepeated(List<T> list, int count) {
    return sampleResultWithNoReturn(list, count);
  }

  /**
   * 允许重复
   */
  public static <T extends SampleAble> List<T> sampleWithRepeated(List<T> list, int count) {
    return sampleResult(list, count, SampleUtil::sampleWithRepeated);
  }

  /**
   * 权重（value）总和为10000的随机结果（key）
   */
  private static Integer calculRate(Map<Integer, Integer> map) {
    if (null == map || map.size() == 0) {
      return 0;
    }
    int weightSum = 0;
    for (Integer weight : map.values()) {
      weightSum += weight;
    }
    int random = ThreadLocalRandom.current().nextInt(1, weightSum + 1);
    for (Entry<Integer, Integer> entry : map.entrySet()) {
      if (random <= entry.getValue()) {
        return entry.getKey();
      }
      random -= entry.getValue();
    }
    return 0;
  }

  /**
   * 按权重不放回随机取出多个
   * 按数值分段式实现
   */
  public static <T> List<T> sampleWithNoRepeated(Map<T, Integer> rateMap, int count) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int total = 0;
      for (Entry<T, Integer> entry : rateMap.entrySet()) {
        if (result.contains(entry.getKey())) {
          continue;
        }
        total += entry.getValue();
      }
      int random = ThreadLocalRandom.current().nextInt(1, total + 1);
      for (Entry<T, Integer> entry : rateMap.entrySet()) {
        if (result.contains(entry.getKey())) {
          continue;
        }
        if (random <= entry.getValue()) {
          result.add(entry.getKey());
          break;
        }
        random -= entry.getValue();
      }
    }
    return result;
  }

  /**
   * 按权重放回随机取出多个
   */
  public static <T> List<T> sampleWithNoRepeatedWithPutBack(Map<T, Integer> rateMap, int count) {
    List<T> result = new ArrayList<>();
    int total = 0;
    for (Entry<T, Integer> entry : rateMap.entrySet()) {
      total += entry.getValue();
    }
    for (int i = 0; i < count; i++) {
      int random = ThreadLocalRandom.current().nextInt(1, total + 1);
      for (Entry<T, Integer> entry : rateMap.entrySet()) {
        if (random <= entry.getValue()) {
          result.add(entry.getKey());
          break;
        }
        random -= entry.getValue();
      }
    }
    return result;
  }

  private static <T extends SampleAble> List<T> sampleResult(List<T> list, int count,
      BiFunction<EnumeratedIntegerDistribution, Integer, List<Integer>> function) {
    if (Objects.isNull(list) || list.isEmpty()) {
      return new ArrayList<>();
    }
    if (list.size() < count) {
      log.warn("data less than count: data size={} count={}", list.size(), count);
      return new ArrayList<>();
    }

    Map<Integer, T> data = IntStream.range(0, list.size()).boxed()
        .collect(Collectors.toMap(i -> i, list::get));

    EnumeratedIntegerDistribution distribution = generateEnumerated(list, data);

    List<Integer> indexes = function.apply(distribution, count);
    return indexes.stream().map(data::get).collect(Collectors.toList());
  }

  // 不放回
  private static <T extends SampleAble> List<T> sampleResultWithNoReturn(List<T> list, int count) {
    if (Objects.isNull(list) || list.isEmpty()) {
      return new ArrayList<>();
    }
    if (list.size() < count) {
      log.warn("data less than count: data size={} count={}", list.size(), count);
      return new ArrayList<>();
    }

    return IntStream.range(0, count).mapToObj(i -> sampleOneWithNoReturn(list))
        .collect(Collectors.toList());
  }

  private static <T extends SampleAble> T sampleOneWithNoReturn(List<T> list) {
    Map<Integer, T> data = IntStream.range(0, list.size()).boxed()
        .collect(Collectors.toMap(i -> i, list::get));

    EnumeratedIntegerDistribution distribution = generateEnumerated(list, data);
    int index = distribution.sample();
    T t = data.get(index);
    data.remove(index);
    list.remove(index);
    return t;
  }

  private static List<Integer> sampleWithNoRepeated(EnumeratedIntegerDistribution distribution,
      int size) {
    if (Objects.isNull(distribution) || size <= 0) {
      return new ArrayList<>();
    }

    Set<Integer> unique = new HashSet<>(size);
    int count = 0;
    while (unique.size() < size) {
      unique.add(distribution.sample());
      count++;
    }

    log.debug("loop: count={}", count);
    return new ArrayList<>(unique);
  }

  private static List<Integer> sampleWithRepeated(EnumeratedIntegerDistribution distribution,
      int size) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(distribution.sample());
    }
    return result;
  }

  private static <T extends SampleAble> EnumeratedIntegerDistribution generateEnumerated(
      List<T> list, Map<Integer, T> tempMap) {

    double sum = list.stream().mapToInt(SampleAble::getWeight).sum();

    List<Double> probList = list.stream().map(SampleAble::getWeight).map(value -> value / sum)
        .collect(Collectors.toList());

    return new EnumeratedIntegerDistribution(
        tempMap.keySet().stream().mapToInt(Integer::intValue).toArray(),
        probList.stream().mapToDouble(Double::doubleValue).toArray()
    );
  }
}
