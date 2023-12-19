package com.github.kuangcp.app.ftp;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author kuangchengping@sinohealth.cn 
 * 2023-12-19 14:31
 */
@Slf4j
public class FtpClientTest {

    private FtpClient ftpClient;

    @Before
    public void setup() throws IOException {
        ftpClient = new FtpClient("localhost", 2121, "test", "test");
        ftpClient.open();
    }

    @After
    public void teardown() throws IOException {
        ftpClient.close();
    }

    @Test
    public void testList() throws IOException {
        Collection<String> files = ftpClient.listFiles("/ftp");
//        assertThat(files).contains("foobar.txt");
        for (String file : files) {
            log.info(file);
        }
    }
}
