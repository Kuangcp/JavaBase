package com.github.kuangcp.strcuture.stackapp;


import com.github.kuangcp.strcuture.stack.MythLinkedStack;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:01 用栈来消除递归，就是模拟递归中系统给他的栈 递归的每一次退出函数， 就是出栈
 * 每一次进入函数 ，就是进栈
 */
public class StackReplaceRecurrence {

  public static double calculateByStack(int num) {
    MythLinkedStack<Integer> stack = new MythLinkedStack<>();
    stack.push(1);
    int temp = num;

    double sum = 1;
    while (stack.peek() != null) {
      if (temp == 0) {
        sum = sum * stack.pop();
      } else {
        stack.push(temp);
        temp /= 2;
      }
    }
    return sum;
  }

  public static double calculateByRecursive(int n) {
    double m;
    if (n == 0) {
      return 1;
    } else {
      m = n * calculateByRecursive(n / 2);
    }
    return m;
  }
}

