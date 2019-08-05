package com.github.kuangcp.simplex.method;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by Myth on 2017/1/13 0013 - 20:51
 * 将 java 目录和 resources 目录引入 classpath 即可正常运行
 *
 * 例如 IDEA编译目录： ./out/production
 * java -classpath ./classes/:./resources/ com.github.kuangcp.simplex.method.ReadProperties
 *
 * 例如 Gradle 编译目录： ./out/build
 * java -classpath ./classes/java/main/:./resources/main/ com.github.kuangcp.simplex.method.ReadProperties
 */
public class ReadProperties {

  private Properties cfg = new Properties();

  /**
   * @param file 例如 resources 目录下 a.properties
   */
  ReadProperties(String file) {
    try {
      URL resource = this.getClass().getClassLoader().getResource(file);
      if (Objects.isNull(resource)) {
        throw new RuntimeException("file not exist");
      }
      File f = new File(resource.getPath());
      cfg.load(new FileInputStream(f));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  String getString(String key) {
    return cfg.getProperty(key);
  }

  int getInt(String key) {
    return Integer.parseInt(cfg.getProperty(key));
  }

  public double getDouble(String key) {
    return Double.parseDouble(getString(key));
  }

  public static void main(String[] a) {
    ReadProperties read = new ReadProperties("math/SimplexMethod.properties");
    String result = read.getString("Max");
    try {
      //配置文件含中文需要转码
      System.out.println(new String(result.getBytes(StandardCharsets.ISO_8859_1),
          StandardCharsets.UTF_8));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
