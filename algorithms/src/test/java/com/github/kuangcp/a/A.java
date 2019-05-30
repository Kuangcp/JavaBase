package com.github.kuangcp.a;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 * 70
 * @author kuangcp on 18-9-16-下午8:33
 * https://www.jianshu.com/u/cbba9a1a8f0a
 */
@AllArgsConstructor
@ToString
class Temp implements Cloneable{
  private String name;
  private String addr;

  public void a(){
    int a = 9;
    Integer b = 4;
    Long num = Long.valueOf(b);
    try {
      Temp clone = (Temp) this.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

  }
}

@Slf4j
public class A {

  // 爬楼梯

  // TODO 动态规划 找规律 找到.最终结果和输入 的关系

  // 状态转换方程
  private static int n = 6;

  @Test
  public void testSplit(){
    int length = "a,b,c,,".split(",").length;
    System.out.println(length);
    assertThat(length, equalTo(3));

    Temp temp = new Temp("1", "1");
    temp.a();

  }

  @Test
  public void testA() {
    helper(1, n);
    helper(2, n);

    log.info(": count={}", count);
  }

  @Test
  public void testB() {
    int result = dp(n);
    log.info(": result={}", result);
  }

  @Test
  public void testC() {
    int[] list = new int[n + 1];
    int result = dp2(n, list);
    log.info(": result={}", result);
  }

  private static int count = 0;

  private void helper(int step, int n) {
    if (step > n) {
      return;
    }
    if (step == n) {
      count++;
    }
    helper(step + 1, n);
    helper(step + 2, n);

  }

  // f(n) = f(n-1) + f(n-2) 具有大量重复计算
  private int dp(int n) {
    if (n <= 2) {
      return n;
    }
    return dp(n - 1) + dp(n - 2);
  }

  /**
   * use cache
   */
  private int dp2(int n, int list[]) {
    if (n <= 2) {
      return n;
    }
    if (0 == list[n]) {
      list[n] = dp2(n - 1, list) + dp2(n - 2, list);
    }

    return list[n];
  }

}
