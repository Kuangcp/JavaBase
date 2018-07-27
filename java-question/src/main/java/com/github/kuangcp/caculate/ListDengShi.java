package com.github.kuangcp.caculate;

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
public class ListDengShi {

  private static int[] datas;
  private static List<String> result = new ArrayList<>();
  private static Map<String, String> results = new HashMap<>();
  private static int num;//记录数据大小


  public static void main(String[] a) {
    datas = new int[]{6, 3, 2, 4, 8, 2, 1, 2, 1, 3, 2};
    num = datas.length;

    ListDengShi test = new ListDengShi();
    test.calculate("", 0);
    test.caculateResult(result);

    System.out.println("组合结果数 ：" + result.size());
    Scanner sc = new Scanner(System.in);
    System.out.println("请输入查询结果");
    String target = sc.nextLine();
    System.out.println(results.getOrDefault(target, "没有该结果"));
  }

  /**
   * 递归求出可能的等式
   */
  public void calculate(String fuhao, int index) {
    if (index < num) {
      index++;
      calculate(fuhao + "+", index);
      calculate(fuhao + "-", index);
    } else {
      result.add(fuhao);
    }
  }

  /**
   * 根据等式计算出值
   */
  public void caculateResult(List<String> dengshi) {
    for (String row : dengshi) {
      if (row.length() >= num) {
        int temp = 0;
        StringBuilder buffer = new StringBuilder();
//            System.out.println("fuhao 长度："+row.length());
        for (int j = 0; j < row.length(); j++) {
          String fuhao = row.charAt(j) + "";
          if ("+".equals(fuhao)) {
            temp += datas[j];
//                        System.out.print("temp:"+temp);
          } else {
            temp -= datas[j];
          }
          buffer.append(fuhao).append(datas[j]);
//                    System.out.print(fuhao +""+datas[j]);
        }
        buffer.append(" = ").append(temp);
//                System.out.println(" = " + temp);
        results.put(temp + "", buffer.toString());
      }
    }
  }
}
