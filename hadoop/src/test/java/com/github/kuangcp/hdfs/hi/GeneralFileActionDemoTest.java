package com.github.kuangcp.hdfs.hi;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-20 11:28
 */
@Slf4j
public class GeneralFileActionDemoTest {

    private static final String HDFS_HOST = "hdfs://172.16.16.80:8020";

    @Test
    public void testCreateDirectory() throws Exception {
        boolean result = GeneralFileActionDemo.createDirectory(HDFS_HOST + "/flink-batch/test");
        assertThat(result, equalTo(true));
    }

    @Test
    public void testCreateFile() throws IOException, InterruptedException {
        GeneralFileActionDemo.createNewFile(HDFS_HOST + "/input/b.md", "fdasfasdfasd");
    }

    @Test
    public void testList() throws IOException {
        List<String> files = GeneralFileActionDemo.listFiles(HDFS_HOST + "/flink-batch");
        files.forEach(v -> {
            log.info("{}", v);
            try {
                GeneralFileActionDemo.deleteByURL(v);
            } catch (IOException e) {
                log.error("", e);
            }
        });
    }

    @Test
    public void testDelete() throws IOException {
        boolean result = GeneralFileActionDemo
                .deleteByURL(HDFS_HOST + "/flink-batch/1559008370602_MONTH_2019-03-01_2019-03-27_SPU.csv");
        System.out.println(result);
    }

    @Test
    public void testRead() throws Exception {
        String url = "/flink-batch/1559008370602_MONTH_2019-03-01_2019-03-27_SPU.csv";
        GeneralFileActionDemo.readHDFSFile(HDFS_HOST + url);
    }
}