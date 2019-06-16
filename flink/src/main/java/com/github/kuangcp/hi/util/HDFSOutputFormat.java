package com.github.kuangcp.hi.util;

import com.github.kuangcp.hi.domain.CalculateVO;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.io.RichOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

/**
 * @author kuangchengping@qipeipu.com
 * @since 2019-05-30 20:36
 */
@lombok.Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class HDFSOutputFormat extends RichOutputFormat<Tuple2<String, Integer>> {

  private CalculateVO partitionVO;
  private long createTime = System.currentTimeMillis();
  private List<Tuple2<String, Integer>> resultList = new LinkedList<>();

  public HDFSOutputFormat(CalculateVO partitionVO) {
    this.partitionVO = partitionVO;
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
    } catch (Exception e) {
      log.error("创建文件失败, HDFS 服务不可用");
      log.error(e.getMessage(), e);
    }
  }
}
