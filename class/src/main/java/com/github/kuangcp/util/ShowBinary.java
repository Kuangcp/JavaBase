package com.github.kuangcp.util;

/**
 * @author kuangcp on 3/14/19-2:26 PM
 */
public class ShowBinary {

  public static String toBinary(Byte value) {
    return Integer.toBinaryString(value);
  }

  public static String toBinary(Integer value) {
    return Integer.toBinaryString(value);
  }

  public static String toBinary(Double value) {
    return Long.toBinaryString(Double.doubleToRawLongBits(value));
  }

  public static String toBinary(Float value) {
    return Float.toHexString(value);
  }
}
