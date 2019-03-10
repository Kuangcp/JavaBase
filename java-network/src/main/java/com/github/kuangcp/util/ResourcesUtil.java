package com.github.kuangcp.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/10/19-11:42 PM
 */
public class ResourcesUtil {

  public static void close(Closeable... closeable) throws IOException {
    if (Objects.isNull(closeable)) {
      return;
    }
    for (Closeable target : closeable) {
      if (Objects.isNull(target)) {
        continue;
      }
      target.close();
    }
  }
}
