package com.github.kuangcp.simpleMethod.number;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by Myth on 2017/1/13 0013 - 20:51
 * FIXME 读取不到文件
 */
public class ReadProperties {

  private Properties cfg = new Properties();

  /**
   * 输入的是从src开始的路径: src/main/resources/a.properties
   */
  public ReadProperties(String file) {
    try {
      File f = new File(file);
      cfg.load(new FileInputStream(f));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public String getString(String key) {
    return cfg.getProperty(key);
  }

  public int getInt(String key) {
    return Integer.parseInt(cfg.getProperty(key));
  }

  public double getDouble(String key) {
    return Double.parseDouble(getString(key));
  }

  public static void main(String[] a) {
    ReadProperties read = new ReadProperties("src/main/resources/math/SimplexMethod.properties");
    String result = read.getString("78");
    try {
      //配置文件含中文需要转码
      System.out.println(new String(result.getBytes(StandardCharsets.ISO_8859_1),
          StandardCharsets.UTF_8));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
