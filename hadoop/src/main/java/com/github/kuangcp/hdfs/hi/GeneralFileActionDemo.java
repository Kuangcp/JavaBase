package com.github.kuangcp.hdfs.hi;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a>
 * @date 2019-05-20 11:15
 */
@Slf4j
public class GeneralFileActionDemo {

    public static boolean createDirectory(String url) throws IOException {
        FileSystem fileSystem = FileSystem.get(getConfig(url));
        boolean result = fileSystem.mkdirs(new Path(url));
        log.info("result={}", result);
        return result;
    }

    public static void createNewFile(String url, String content)
            throws IOException, InterruptedException {
        FileSystem fs = FileSystem.get(getConfig(url));

        FSDataOutputStream os = fs.create(new Path(URI.create(url).getPath()));
        os.write(content.getBytes(StandardCharsets.UTF_8));
        os.flush();
        TimeUnit.SECONDS.sleep(4);
        os.close();
        fs.close();
    }

    public static List<String> listFiles(String url) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(url), getConfig(url));
        Path path = new Path(url);
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

    public static boolean deleteByURL(String url) throws IOException {
        FileSystem fs = FileSystem.get(getConfig(url));
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
     * <p>
     * notice that the url is the full path name
     */
    public static void readHDFSFile(String url) throws Exception {
        FileSystem fs = FileSystem.get(getConfig(url));
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

    private static Configuration getConfig(String url) {
        Configuration config = new Configuration();
        config.set("fs.defaultFS", getHost(url));

        return config;
    }

    private static String getHost(String url) {
        URI uri = URI.create(url);
        return uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort();
    }
}
