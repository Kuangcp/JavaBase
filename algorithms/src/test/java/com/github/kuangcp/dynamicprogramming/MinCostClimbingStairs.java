package com.github.kuangcp.dynamicprogramming;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-09-03 12:39
 *
 * https://leetcode-cn.com/problems/min-cost-climbing-stairs/description/
 */
public class MinCostClimbingStairs {


  @Test
  public void testRecursion() {
    int[] cost = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
    int result = recursion(cost.length, cost);
    assertThat(result, equalTo(6));
  }

  /**
   * 关键:
   * 1.可以从0 或者 1位置为起始点！
   * 2.梯顶不是最后一个数 而是最后一个数后一个位置！
   * dp[i] = min(dp[i-2] + cost[i-2], dp[i-1] + cost[i-1]) (dp[i]代表“到达”第i层所需最小花费)
   * 自底向上(bottom-up)
   */
  public static int recursion(int totalStep, int[] cost) {
    if (0 == totalStep || 1 == totalStep) {
      return 0;
    }

    return Math.min(recursion(totalStep - 2, cost) + cost[totalStep - 2],
        recursion(totalStep - 1, cost) + cost[totalStep - 1]);
  }

  /**
   * 关键:
   * 1.可以从0 或者 1位置为起始点！
   * 2.梯顶不是最后一个数 而是最后一个数后一个位置！
   * dp[i] = min(dp[i-2] + cost[i-2], dp[i-1] + cost[i-1]) (dp[i]代表“到达”第i层所需最小花费)
   * 自底向上(bottom-up)
   */
  public static int dpRecursion(int totalStep, int[] cost, int[] dp) {
    if (0 == totalStep || 1 == totalStep) {
      return 0;
    }

    if (0 == dp[totalStep]) {
      dp[totalStep] = Math.min(dpRecursion(totalStep - 2, cost, dp) + cost[totalStep - 2],
          dpRecursion(totalStep - 1, cost, dp) + cost[totalStep - 1]);
    }
    return dp[totalStep];
  }


  /**
   * 关键:
   * 1.可以从0 或者 1位置为起始点！
   * 2.梯顶不是最后一个数 而是最后一个数后一个位置！
   * dp[i] = min(dp[i-2] + cost[i-2], dp[i-1] + cost[i-1]) (dp[i]代表“到达”第i层所需最小花费)
   * 自底向上(bottom-up)
   * 改为循环
   */
  public static int dpLoop(int[] cost) {
    int[] dp = new int[cost.length + 1];
    for (int i = 2; i < cost.length + 1; ++i) {
      dp[i] = Math.min(dp[i - 2] + cost[i - 2], dp[i - 1] + cost[i - 1]);
    }
    return dp[cost.length];
  }

  /**
   * 关键:
   * 1.可以从0 或者 1位置为起始点！
   * 2.梯顶不是最后一个数 而是最后一个数后一个位置！
   * dp[i] = cost[i] + min(dp[i- 1], dp[i-2]) (dp[i]代表“经过”第i层所需最小花费)
   * 自底向上(bottom-up)
   * 改为循环
   * 优化内存使用(滚动数组---只使用每一轮计算所需的缓存，通常是上一轮或者多轮的结果)
   * 分析可得 只需要两个int变量交替使用即可达到要求
   */
  public static int dpLoopLessMemory(int[] cost) {
    int temp1 = cost[0];
    int temp2 = cost[1];
    int res = 0;
    for (int i = 2; i < cost.length; ++i) {
      res = cost[i] + Math.min(temp1, temp2);
      temp1 = temp2;
      temp2 = res;
    }
    return Math.min(temp1, temp2);
  }
}
