package com.github.kuangcp.singleton;

/**
 * 双重检查[推荐用]
 *
 * @author kuangcp on 2019-04-11 12:41 PM
 */
public class DoubleCheckWithVolatile {

  private static volatile DoubleCheckWithVolatile singleton;

  private DoubleCheckWithVolatile() {
  }

  public static DoubleCheckWithVolatile getInstance() {
    if (singleton == null) {
      synchronized (DoubleCheckWithVolatile.class) {
        if (singleton == null) {
          singleton = new DoubleCheckWithVolatile();
        }
      }
    }
    return singleton;
  }
}
