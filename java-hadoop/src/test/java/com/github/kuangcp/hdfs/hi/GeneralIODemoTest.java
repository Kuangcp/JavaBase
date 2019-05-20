package com.github.kuangcp.hdfs.hi;

import java.io.IOException;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-20 11:28
 */
public class GeneralIODemoTest {

  @Test
  public void testCreateDirectory() throws Exception {
    GeneralIODemo.createDirectory();
  }

  @Test
  public void testCreateFile() throws IOException, InterruptedException {
    GeneralIODemo.createNewHDFSFile("/input/b.md", "fdasfasdfasd");
  }

  @Test
  public void testList() throws IOException {
//    GeneralIODemo.listFiles("hdfs://172.16.16.80:8020/input");
    GeneralIODemo.listFiles("hdfs://localhost:8020/input");
  }

  @Test
  public void testDelete() throws IOException {
    boolean result = GeneralIODemo.deleteHDFSFile("hdfs://172.16.16.80:8020/input/b.md");
    System.out.println(result);
  }
}