package com.github.kuangcp.strcuture.stackapp;


import com.github.kuangcp.strcuture.stack.MythLinkedStack;

import java.util.Scanner;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:01
 * 用栈来消除递归，就是模拟递归中系统给他的栈
 * 递归的每一次退出函数， 就是出栈
 * 每一次进入函数 ，就是进栈
 */
public class StackReplaceRecurrence {

  public static void main(String[] args) {
    MythLinkedStack<Integer> Q = new MythLinkedStack<>();
    Q.push(1);
    Scanner sc = new Scanner(System.in);
    System.out.println("请输入函数自变量的值");
    int m = sc.nextInt();
    int n = m;

    double sum = 1;
    //先把实例化语句写出来，再选中用Ctrl+shift+o 快速导入包，注意不能跨项目
    while (Q.peek() != null) {
      if (n == 0) {
        sum = sum * Q.pop();
      } else {
        Q.push(n);
        n /= 2;
      }
    }
    System.out.println("函数的值是：" + sum);

    System.out.println("函数的值是：" + f(m));//终于知道错误了，我擦，上面的代码已经将n改变成了0了
    sc.close();

  }

  private static double f(int n) {
    double m;
    if (n == 0) {
      return 1;
    } else {
      m = n * f(n / 2);
    }
    return m;
  }
}

