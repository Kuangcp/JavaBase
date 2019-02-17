package base.tryblock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * created by https://gitee.com/gin9
 *
 *  try-with-resources
 * as long as the object implements the AutoCloseable and Closeable interface
 *
 * @author kuangcp on 18-10-1-下午9:59
 */
public class TWR {

  // primitive way
  static String ReadFile(String file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    try {
      return br.readLine();
    } finally {
      br.close();
    }
  }

  // use TWR
  static String ReadFileWithTWR(String file) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      return br.readLine();
    }
  }

}
