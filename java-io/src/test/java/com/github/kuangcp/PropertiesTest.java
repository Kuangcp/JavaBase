package com.github.kuangcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-21-下午4:51
 */
public class PropertiesTest {

  @Test
  public void testRead() throws IOException {
    Properties properties = new Properties();
//    String path = Properties.class.getResource("./properties/main.properties").getPath();
    System.out.println(new File("").getAbsolutePath());
    properties.load(new FileInputStream("src/test/resources/properties/main.properties"));

    String a = properties.getProperty("A");
    String b = new String(properties.getProperty("B").getBytes(StandardCharsets.ISO_8859_1),
        StandardCharsets.UTF_8);
    System.out.println(a + b);

  }
}
