package com.github.kuangcp.hi;

import com.github.kuangcp.hi.domain.CalculateVO;
import com.github.kuangcp.hi.util.HDFSOutputFormat;
import com.github.kuangcp.hi.util.SourceProvider;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 将数据格式化并输出到HDFS
 *
 * @author kuangchengping@qipeipu.com
 * @since 2019-05-16 16:26
 */
@Slf4j
public class PartsSpuStatistic {

  private static final ExecutionEnvironment ENV;

  static {
    ENV = ExecutionEnvironment.getExecutionEnvironment();
  }

  public static void main(String[] args) throws Exception {
    try {
      if (Objects.isNull(args) || args.length != 1) {
        log.warn("invalid param: args={} {}", args, "");
        return;
      }

      String originStr = new String(Base64.getDecoder().decode(args[0]), StandardCharsets.UTF_8);
      log.info("command: originStr={}", originStr);

      calculateOrder();
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
    }
    // 必须是方法的最后一行 否则 Flink 的执行计划无法生成
    ENV.execute("PartsSpuStatistic");
  }

  @SuppressWarnings("deprecation")
  static void calculateOrder() throws Exception {

    calculateAndAggregate(new CalculateVO());
  }

  /**
   * 分页计算并聚合
   */
  private static void calculateAndAggregate(CalculateVO partitionVO) {
    AggregateOperator<Tuple2<String, Integer>> result = null;

    SourceProvider provider = new SourceProvider(partitionVO);
    while (provider.hasNextPage()) {
      List<String> data = provider.generateResource();
      DataSource<String> source = ENV.fromCollection(data);

      DataSet<Tuple2<String, Integer>> counts = source
          .filter(Objects::nonNull)
          .map(new Mapper())
          .groupBy(0)
          .sum(1);

      if (Objects.isNull(result)) {
        result = counts.groupBy(0).sum(1);
      } else {
        DataSet<Tuple2<String, Integer>> temp = result.union(counts);
        result = temp.groupBy(0).sum(1);
      }
    }

    if (Objects.isNull(result)) {
      return;
    }

    result.output(new HDFSOutputFormat(partitionVO));
  }

  public static final class Mapper implements
      MapFunction<String, Tuple2<String, Integer>> {

    @Override
    public Tuple2<String, Integer> map(String value) {
      return new Tuple2<>(value, 1);
    }
  }
}
