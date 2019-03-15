package com.github.kuangcp.util;

/**
 * @author kuangcp on 3/14/19-2:26 PM
 */
public class ShowBinary {

  public static String toBinaryString(Integer value) {
    return Integer.toBinaryString(value);
  }

  public static String toBinaryString(Double value) {
    return Long.toBinaryString(Double.doubleToRawLongBits(value));
  }

  public static String toBinaryString(Float value) {
    return Float.toHexString(value);
  }
}
