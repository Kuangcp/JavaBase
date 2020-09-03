package com.github.kuangcp.dynamicprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9 70
 *
 * https://www.jianshu.com/u/cbba9a1a8f0a
 *
 * https://leetcode-cn.com/problems/climbing-stairs/description/
 *
 * @author kuangcp on 18-9-16-下午8:33
 */
@Slf4j
public class ClimbStairs {

  // 爬楼梯
  // 动态规划 找规律 找到.最终结果和输入 的关系 以及 状态转换方程

  /**
   * 总台阶数
   */
  private static final int totalStep = 3;

  private static int count = 0;

  @Test
  public void testRecursion() {
    // 第一次 1 个台阶的方式
    recursionStep(1, totalStep);
    // 第一次 2 个台阶的方式
    recursionStep(2, totalStep);

    log.info("count={}", count);
  }

  @Test
  public void testDP() {
    int result = dp(totalStep);
    log.info(": result={}", result);
  }

  @Test
  public void testDPOptimize() {
    int[] list = new int[totalStep + 1];
    int result = dp2(totalStep, list);
    log.info(": result={}", result);
  }

  private void recursionStep(int step, int n) {
    if (step > n) {
      return;
    }
    if (step == n) {
      count++;
    }
    recursionStep(step + 1, n);
    recursionStep(step + 2, n);
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
  private int dp2(int n, int[] list) {
    if (n <= 2) {
      return n;
    }
    if (0 == list[n]) {
      list[n] = dp2(n - 1, list) + dp2(n - 2, list);
    }

    return list[n];
  }
}
