package com.github.kuangcp.hdfs.hi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-20 11:15
 */
public class GeneralIODemo {

  private static Configuration config;

  static {
    System.setProperty("hadoop.home.dir", "/");
    config = new Configuration();
//    config.set("fs.defaultFS", "hdfs://127.0.0.1:8020");
    config.set("fs.defaultFS", "hdfs://172.16.16.80:8020");
  }

  public static void createDirectory() throws IOException {
    FileSystem fileSystem = FileSystem.get(config);
    String directoryName = "/hadoop/dfs/name/javadeveloperzone";
    Path path = new Path(directoryName);
    fileSystem.mkdirs(path);
  }

  public static void createNewHDFSFile(String toCreateFilePath, String content)
      throws IOException, InterruptedException {
    FileSystem fs = FileSystem.get(config);

    FSDataOutputStream os = fs.create(new Path(toCreateFilePath));
    os.write(content.getBytes(StandardCharsets.UTF_8));
    os.flush();
    TimeUnit.SECONDS.sleep(4);
    os.close();
    fs.close();
  }

  public static void listFiles(String DirFile) throws IOException {
    FileSystem fs = FileSystem.get(URI.create(DirFile), config);
    Path path = new Path(DirFile);
    FileStatus[] status = fs.listStatus(path);

    //方法1
    for (FileStatus f : status) {
      System.out.println(f.getPath().toString());
    }

    //方法2
//    Path[] listedPaths = FileUtil.stat2Paths(status);
//    for (Path p : listedPaths) {
//      System.out.println(p.toString());
//    }
  }

  public static boolean deleteHDFSFile(String dst) throws IOException {
    FileSystem fs = FileSystem.get(config);
    Path path = new Path(dst);
    boolean isDeleted = fs.deleteOnExit(path);
    fs.close();
    return isDeleted;
  }

  public static void writeFileToHDFS() throws IOException {
    Configuration configuration = new Configuration();
    configuration.set("fs.defaultFS", "hdfs://localhost:9000");
    FileSystem fileSystem = FileSystem.get(configuration);
    //Create a path
    String fileName = "read_write_hdfs_example.txt";
    Path hdfsWritePath = new Path("/user/javadeveloperzone/javareadwriteexample/" + fileName);
    FSDataOutputStream fsDataOutputStream = fileSystem.create(hdfsWritePath, true);
    BufferedWriter bufferedWriter = new BufferedWriter(
        new OutputStreamWriter(fsDataOutputStream, StandardCharsets.UTF_8));
    bufferedWriter.write("Java API to write data in HDFS");
    bufferedWriter.newLine();
    bufferedWriter.close();
    fileSystem.close();
  }
}
