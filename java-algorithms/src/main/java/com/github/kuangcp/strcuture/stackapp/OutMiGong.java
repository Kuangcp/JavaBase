package com.github.kuangcp.strcuture.stackapp;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:11
 * 迷宫，找出路径和最短路路径
 * 想要实现和c一样用define定义常量（不是用final定义方法内部常量）的功能就要用接口
 * 在接口中定义一个常量，再继承这个接口，就能在类中到处用这个常量了
 * path的路径没有变过，哪了出错了？、、、
 */
public class OutMiGong {

  private static int top = -1, count = 0, minlen = 100;
  //定义一个普通整型的地图，两个栈
  //int [][] map = new int [M+2][N+2];  不需要指明大小
  private static int[][] map = {
      {1, 1, 1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 0, 0, 0, 1},
      {1, 1, 0, 1, 1, 1, 0, 1},
      {1, 0, 0, 0, 0, 0, 1, 1},
      {1, 0, 1, 1, 1, 1, 0, 1},
      {1, 0, 0, 0, 0, 0, 0, 1},
      {1, 0, 0, 1, 0, 1, 0, 1},
      {1, 1, 1, 1, 1, 1, 1, 1}
  };

  private static Point[] Stack = new Point[100];//只是说放的是point类型的引用，而不是放对象
  private static Point[] Path = new Point[100];

  static class Point {

    int row = 1;//行
    int col = 1;//列
    int choose = -1;//记录岔路口选择状态
  }

  public static void main(String[] args) {
    for (int k = 0; k < 100; k++) {
      Stack[k] = new Point();
      Path[k] = new Point();
    }
    int found, choose, i, j;
    boolean success = false;
    int maxRow = 6;
    int maxCol = 6;
    char[][] print = new char[maxRow + 2][maxCol + 2];
    for (int z = 0; z < maxRow + 2; z++) {
      for (int x = 0; x < maxCol + 2; x++) {
        print[z][x] = (char) map[z][x];
      }
    }
    System.out.println(Stack[6].row);//为什么会说空指向呢?
    top++;
    Stack[top].row = 1;
    Stack[top].col = 1;    //起始位置是（1,1）
    Stack[top].choose = 0;
    map[1][1] = -1;

    while (top > -1) {   //栈不为空就循环
      i = Stack[top].row;
      j = Stack[top].col;
      choose = Stack[top].choose;
      if (i == maxRow && j == maxCol) {   //走到了终点
        success = true;
        System.out.printf("    >>> %d:  ", count++);
        for (int k = 0; k <= top; k++) {
          System.out.printf("(%d,%d)  ", Stack[k].row, Stack[k].col);
          if ((k + 1) % 8 == 0) {
            System.out.printf("\n");    //每8个换行
          }
        }
        System.out.println("\n");
        if (top + 1 < minlen) {                     //比较 找到最短路径放入path中
          System.arraycopy(Stack, 0, Path, 0, top + 1);
          minlen = top + 1;
        }
        map[Stack[top].row][Stack[top].col] = 0;    // 把破坏的那个终点恢复
        top--;
        i = Stack[top].row;
        j = Stack[top].col;
        choose = Stack[top].choose;
      }
      found = 0;
      while (choose < 4 && found == 0) {   //开始选择路径
        switch (choose++) {
          case 0:
            i = Stack[top].row - 1;
            j = Stack[top].col;
            break;    // 上
          case 1:
            i = Stack[top].row;
            j = Stack[top].col + 1;
            break;   // 右
          case 2:
            i = Stack[top].row + 1;
            j = Stack[top].col;
            break;   // 下
          case 3:
            i = Stack[top].row;
            j = Stack[top].col - 1;
            break;    // 左
        }
        if (map[i][j] == 0) {
          found = 1;
        }
      }
      //System.out.println("top="+top);
      if (found == 1) {
        Stack[top].choose = choose;
        top++;
        Stack[top].row = i;
        Stack[top].col = j;
        Stack[top].choose = 0;
        map[i][j] = -1;    //破坏该位置
      } else {   //没找到
        map[Stack[top].row][Stack[top].col] = 0;
        //恢复当前位置，在while和方向choose变量的作用下就能一步步按原路返回
        top--;
      }
    }

    if (success) {
      System.out.printf("最短路径的长度 %d\n\n", minlen);
      System.out.print("最短的路径：");
      for (int k = 0; k < minlen; k++) {
        System.out.printf("(%d,%d)  ", Path[k].row, Path[k].col);
        print[Path[k].row][Path[k].col] = '.';
        if ((k + 1) % 8 == 0) {
          System.out.println();
        }
      }
      System.out.print("\n迷宫地图：\n");
      for (int z = 0; z < maxRow + 2; z++) {
        for (int x = 0; x < maxCol + 2; x++) {
          System.out.printf(" %c", map[z][x]);
        }
        System.out.println();
      }
      System.out.println();
      System.out.printf("\n最短路径如图：\n");
      for (int z = 0; z < maxRow + 2; z++) {
        for (int x = 0; x < maxCol + 2; x++) {
          System.out.printf(" %c", print[z][x]);
        }
        System.out.println();
      }
    } else {
      System.out.printf("这个迷宫不能到达出口");
    }
  }
}

