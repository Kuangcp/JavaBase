package com.github.kuangcp.hi;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.io.RichOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

/**
 * 模拟输出到文件
 *
 * @author https://github.com/kuangcp
 * @since 2019-05-30 20:36
 */
@lombok.Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class SimpleSink extends RichOutputFormat<Tuple2<String, Integer>> {

  private String name;
  private long createTime = System.currentTimeMillis();
  // 如果给属性加上 transient 修饰, 就会报错 因为无法同步数据
  private List<Tuple2<String, Integer>> resultList = new LinkedList<>();

  SimpleSink(String name) {
    this.name = name;
  }

  @Override
  public void configure(Configuration parameters) {
  }

  @Override
  public void open(int taskNumber, int numTasks) throws IOException {
  }

  @Override
  public void writeRecord(Tuple2<String, Integer> record) throws IOException {
    resultList.add(record);
  }

  @Override
  public void close() {
    try {
      resultList.forEach(t -> log.info("close by sink: name={} count={}", t.f0, t.f1));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
