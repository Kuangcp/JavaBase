package com.github.kuangcp.hdfs.hi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-20 11:28
 */
@Slf4j
public class GeneralFileActionDemoTest {

  @Test
  public void testCreateDirectory() throws Exception {
    boolean result = GeneralFileActionDemo.createDirectory("/flink-batch/test");
    assertThat(result, equalTo(true));
  }

  @Test
  public void testCreateFile() throws IOException, InterruptedException {
    GeneralFileActionDemo.createNewFile("/input/b.md", "fdasfasdfasd");
  }

  @Test
  public void testList() throws IOException {
    List<String> files = GeneralFileActionDemo.listFiles("/flink-batch");
    files.forEach(v -> {
      log.info("{}", v);
//      try {
//        GeneralFileActionDemo.deleteByURL(v);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
    });
  }

  @Test
  public void testDelete() throws IOException {
    boolean result = GeneralFileActionDemo.deleteByPath("/input/b.md");
    System.out.println(result);
  }

  @Test
  public void testRead() throws Exception {
    // hdfs://172.16.16.80:8020
    String url = "/flink-batch/SEASON2018-10-01_2018-12-31.csv";
    GeneralFileActionDemo.readHDFSFile(url);
  }
}