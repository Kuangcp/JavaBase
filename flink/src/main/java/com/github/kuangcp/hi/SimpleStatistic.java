package com.github.kuangcp.hi;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author https://github.com/kuangcp
 * @since 2019-05-16 16:26
 */
@Slf4j
public class SimpleStatistic {

  private static final ExecutionEnvironment ENV;
  private static final int taskNum = 1;

  static {
    ENV = ExecutionEnvironment.getExecutionEnvironment();
  }

  public static void main(String[] args) throws Exception {
    log.info("start calculate");
    try {
      for (int i = 0; i < taskNum; i++) {
        calculateAndAggregate("batch-" + i);
      }
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
    }

    ENV.getConfig().setAutoWatermarkInterval(1000);
    ENV.execute("PartsSpuStatistic");

    JobExecutionResult lastJobExecutionResult = ENV.getLastJobExecutionResult();
    lastJobExecutionResult.getAllAccumulatorResults()
        .forEach((k, v) -> log.error("failed : k={} {}", k, v));
    if (lastJobExecutionResult.getAllAccumulatorResults().isEmpty()) {
      log.info("run success");
    } else {
      log.error("run failed");
    }

    System.exit(1);
  }

  /**
   * 分页计算并聚合
   */
  private static void calculateAndAggregate(String batchId) {
    AggregateOperator<Tuple2<String, Integer>> result = null;

    SimpleSource provider = new SimpleSource(batchId);
    while (provider.hasNextPage()) {
      List<String> data = provider.generateResource();
      DataSource<String> source = ENV.fromCollection(data);

      // 合并窗口内数据
      DataSet<Tuple2<String, Integer>> counts = source
          .filter(Objects::nonNull)
          .map(new Mapper())
          .groupBy(0)
          .sum(1);

      // 当前窗口内和历史数据合并
      if (Objects.isNull(result)) {
        result = counts.groupBy(0).sum(1);
      } else {
        DataSet<Tuple2<String, Integer>> temp = result.union(counts);
        result = temp.groupBy(0).sum(1);
      }
      log.info("aggregate once");
    }

    if (Objects.isNull(result)) {
      return;
    }

    result.output(new SimpleSink(batchId));
  }

  public static final class Mapper implements
      MapFunction<String, Tuple2<String, Integer>> {

    @Override
    public Tuple2<String, Integer> map(String value) {
      return new Tuple2<>(value, 1);
    }
  }
}
