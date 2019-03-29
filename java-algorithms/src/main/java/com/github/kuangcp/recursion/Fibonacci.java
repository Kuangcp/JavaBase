package com.github.kuangcp.recursion;

/**
 * @author kuangcp on 3/28/19-9:28 PM
 * TODO 时间复杂度 如何优化
 */
class Fibonacci {

  int calculate(int num) {
    if (num < 0) {
      return 1;
    }
    return calculate(num - 1) + calculate(num - 2);
  }
}
