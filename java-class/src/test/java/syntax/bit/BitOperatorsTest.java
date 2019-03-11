package syntax.bit;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/5/19-2:41 PM
 * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
 * >>> : https://stackoverflow.com/questions/14501233/unsigned-right-shift-operator-in-java
 */
@Slf4j
public class BitOperatorsTest {

  @Test
  public void testRight() {
    // int 32 long 64 bit

    int i = -7;

    show(i);
    show(i >>> 1);
    show(i >>> 2);
    show(i >>> -1);
    show(i >> 1);
    show(i >> 2);
  }

  @Test
  public void testLeft() {
    int i = -7;

    show(i << 1);
    show(i << 2);
  }

  static void show(int result) {
    log.info("{} {}", result, Integer.toBinaryString(result));
  }

  static void tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    int result = (n < 0) ? 1 : (n >= 100) ? 200 : n + 1;
    log.info("re: result={}", result);
  }
}
