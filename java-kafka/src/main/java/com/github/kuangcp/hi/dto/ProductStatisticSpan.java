package com.github.kuangcp.hi.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ProductStatisticSpan {

  /**
   * 按日统计
   */
  DAY("DAY00_", StatisticSpan.SPAN_DAY),

  /**
   * 按月统计
   */
  MONTH("MONTH_", StatisticSpan.SPAN_MONTH),

  /**
   * 按季度统计
   */
  SEASON("SEASON", StatisticSpan.SPAN_SEASON),

  /**
   * 按年统计
   */
  YEAR("YEAR0_", StatisticSpan.SPAN_YEAR);

  private String prefix;

  private Integer span;

  private static final List<ProductStatisticSpan> ALL = Arrays.asList(values());

  private static final Map<Integer, ProductStatisticSpan> MAP = ALL.stream()
      .collect(Collectors.toMap(ProductStatisticSpan::getSpan, Function.identity()));

  ProductStatisticSpan(String prefix, Integer span) {
    this.prefix = prefix;
    this.span = span;
  }


  public static List<ProductStatisticSpan> getAll() {
    return ALL;
  }

  public static Map<Integer, ProductStatisticSpan> getMap() {
    return MAP;
  }

  public static ProductStatisticSpan getBySpan(Integer span) {
    return MAP.getOrDefault(span, ProductStatisticSpan.SEASON);
  }

}

