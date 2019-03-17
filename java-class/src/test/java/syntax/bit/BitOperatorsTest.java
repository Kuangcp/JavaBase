package syntax.bit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/5/19-2:41 PM
 * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
 * >>> : https://stackoverflow.com/questions/14501233/unsigned-right-shift-operator-in-java
 *
 * 原码=>补码: 取反再加1 除符号位
 * 补码=>原码: 减1再取反 除符号位
 */
@Slf4j
public class BitOperatorsTest {

  @Test
  public void testSimple() {
    // 与 : 1 1 -> 1 否则 0
    assertThat(0b1010 & 0b1001, equalTo(0b1000));

    // 或 : 0 0 -> 0 否则 1
    assertThat(0b101 | 0b001, equalTo(0b101));

    // 异或: 不同 -> 1 否则 0
    assertThat(0b101 ^ 0b001, equalTo(0b100));

    // 非
    assertThat(~0b1110, equalTo(-0b1111));
    assertThat(~0b001, equalTo(-2));

    // 00000000000000000000000000000001
    show(0b001);
    // 11111111111111111111111111111110
    show(~0b001);// 以补码进行存储的, 所以取值时需要转回原码

    // 减一 11111111111111111111111111111101
    // 取反 10000000000000000000000000000010
    // 也就是  -2
  }

  @Test
  public void testBitAndAssignment() {
    int num = 0b101011;
    num |= 0b101;
    assertThat(num, equalTo(0b101111));

    // 101111 | 1011
    num |= num >>> 2;
    assertThat(num, equalTo(0b101111));

    // TODO 
    for (int i = 0; i < 17; i++) {
      int temp = i;
      log.info("origin: temp={}", temp);
      temp |= temp >>> 1;
//      log.info("1: temp={}", temp);

      temp |= temp >>> 2;
//      log.info("2: temp={}", temp);

      temp |= temp >>> 3; // 无效
//      log.info("3: temp={}", temp);

      temp |= temp >>> 4;
//      log.info("4: temp={}", temp);

      System.out.println(temp);

    }
  }

  @Test
  public void testRight() {
    // 带符号的 右移操作 正数则左补0 负数则左补1
    assertThat(0b101 >> 2, equalTo(0b1));

    // 11111111111111111111111111111011 -0b101的补码表示(也是实际存储的值)
    show(-0b101);
    // 11111111111111111111111111111110 右移两位
    // 10000000000000000000000000000010 -1 取反 得到 原码
    assertThat(-0b101 >> 2, equalTo(-0b10));

    // 无视符号位的 右移操作 左补0
    assertThat(0b101 >>> 2, equalTo(0b1));
    assertThat(0b101 >>> 3, equalTo(0b0));

    // 11111111111111111111111111111011
    // 00000000000000000000000000000011
    show(-0b101 >>> 30);
    assertThat(-0b101 >>> 30, equalTo(0b11));

    // 在正数情况下 >> 等价于 >>>
    assertThat(0b110011 >> 3, equalTo(0b110011 >>> 3));
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
