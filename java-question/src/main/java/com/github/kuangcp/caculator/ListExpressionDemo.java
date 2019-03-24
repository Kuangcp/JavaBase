package com.github.kuangcp.caculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 给出一组数字，只有加减，给你一个数字判断是否能从数组中计算得到
 * 最好列出等式
 * 解决，耗时一个多小时，两个月没有码代码，大脑迟钝太多了！
 * Created by l on 2017/1/3
 */
public class ListExpressionDemo {

  private static int[] dataArray;
  private static List<String> result = new ArrayList<>();
  private static Map<String, String> results = new HashMap<>();
  private static int num;//记录数据大小


  public static void main(String[] a) {
    dataArray = new int[]{6, 3, 2, 4, 8, 2, 1, 2, 1, 3, 2};
    num = dataArray.length;

    ListExpressionDemo test = new ListExpressionDemo();
    test.calculate("", 0);
    test.calculateResult(result);

    System.out.println("组合结果数 ：" + result.size());
    Scanner sc = new Scanner(System.in);
    System.out.println("请输入查询结果");
    String target = sc.nextLine();
    System.out.println(results.getOrDefault(target, "没有该结果"));
  }

  /**
   * 递归求出可能的等式
   */
  private void calculate(String operation, int index) {
    if (index < num) {
      index++;
      calculate(operation + "+", index);
      calculate(operation + "-", index);
    } else {
      result.add(operation);
    }
  }

  /**
   * 根据等式计算出值
   */
  private void calculateResult(List<String> expression) {
    for (String row : expression) {
      if (row.length() >= num) {
        int temp = 0;
        StringBuilder buffer = new StringBuilder();
        System.out.println("operation 长度：" + row.length());
        for (int j = 0; j < row.length(); j++) {
          String operation = row.charAt(j) + "";
          if ("+".equals(operation)) {
            temp += dataArray[j];
            System.out.print("temp:" + temp);
          } else {
            temp -= dataArray[j];
          }
          buffer.append(operation).append(dataArray[j]);
          System.out.print(operation + "" + dataArray[j]);
        }
        buffer.append(" = ").append(temp);
        System.out.println(" = " + temp);
        results.put(temp + "", buffer.toString());
      }
    }
  }
}
