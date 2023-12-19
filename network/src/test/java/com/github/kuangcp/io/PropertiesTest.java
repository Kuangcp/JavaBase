package com.github.kuangcp.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author kuangcp on 18-8-21-下午4:51
 */
@Slf4j
public class PropertiesTest {

    @Test
    public void testRead() throws IOException {
        String path = Properties.class.getResource("/properties/main.properties").getPath();
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));

        String a = properties.getProperty("A");
        String b = properties.getProperty("B");
        String decodedB = new String(b.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        log.info("a={} b=[{}] [{}]", a, b, decodedB);
    }
}
