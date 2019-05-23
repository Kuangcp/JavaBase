package com.github.kuangcp.hdfs.hi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-20 11:15
 */
@Slf4j
public class GeneralFileActionDemo {

  private static String HDFS_HOST;
  private static Configuration config;

  static {
    // "hdfs://127.0.0.1:8020"
    HDFS_HOST = "hdfs://172.16.16.80:8020";

    // warn for what ?
//    System.setProperty("hadoop.home.dir", "/");

    config = new Configuration();
    config.set("fs.defaultFS", HDFS_HOST);
  }

  public static boolean createDirectory(String dirPath) throws IOException {
    FileSystem fileSystem = FileSystem.get(config);
    boolean result = fileSystem.mkdirs(new Path(dirPath));
    log.info("result={}", result);
    return result;
  }

  public static void createNewFile(String toCreateFilePath, String content)
      throws IOException, InterruptedException {
    FileSystem fs = FileSystem.get(config);

    FSDataOutputStream os = fs.create(new Path(toCreateFilePath));
    os.write(content.getBytes(StandardCharsets.UTF_8));
    os.flush();
    TimeUnit.SECONDS.sleep(4);
    os.close();
    fs.close();
  }

  public static List<String> listFiles(String DirFile) throws IOException {
    FileSystem fs = FileSystem.get(URI.create(HDFS_HOST + DirFile), config);
    Path path = new Path(DirFile);
    FileStatus[] status = fs.listStatus(path);

    return Arrays.stream(status).map(v -> v.getPath().toString()).collect(Collectors.toList());
    //方法1
//    for (FileStatus f : status) {
//      log.info(f.getPath().toString());
//    }

    //方法2
//    Path[] listedPaths = FileUtil.stat2Paths(status);
//    for (Path p : listedPaths) {
//      System.out.println(p.toString());
//    }
  }

  public static boolean deleteByPath(String filePath) throws IOException {
    return deleteByURL(HDFS_HOST + filePath);
  }

  public static boolean deleteByURL(String url) throws IOException {
    FileSystem fs = FileSystem.get(config);
    Path path = new Path(url);
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

  /**
   * read the hdfs file content
   *
   * notice that the url is the full path name
   */
  public static void readHDFSFile(String url) throws Exception {
    FileSystem fs = FileSystem.get(config);
    Path path = new Path(url);

    // check if the file exists
    if (!fs.exists(path)) {
      throw new Exception("the file is not found .");
    }

    FSDataInputStream is = fs.open(path);

    // read line by line
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(is, StandardCharsets.UTF_8));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
    }

    // read as byte[]
//      FileStatus stat = fs.getFileStatus(path);
//      byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
//      is.readFully(0, buffer);

    is.close();
    fs.close();
  }
}
