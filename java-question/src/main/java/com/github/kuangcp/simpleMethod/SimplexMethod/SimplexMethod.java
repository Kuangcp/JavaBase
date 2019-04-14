package com.github.kuangcp.simpleMethod.SimplexMethod;


import com.github.kuangcp.simpleMethod.number.ReadProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/20
 * 使用单纯形法来求解最优解
 * 但是还有一个数据类型的问题是无法避免的，一般是用分数是最好的，但是浮点数方便计算。。。
 */
public class SimplexMethod {

  //读取配置文件
  private static ReadProperties config;
  //最大参数个数
  private static int MAXPARAMS;
  //最大行列式数
  private static int EQUALITY;
  //约束式子系数集合
  private List<Equality> Rows = new ArrayList<Equality>();
  //原始式子系数集合
  private List<Double> Max = new ArrayList<Double>();
  //单纯形表的总体数据结构
  private List<Table> Tables = new ArrayList<Table>();
  //单纯形表中间计算结果右侧
  private List<Double> Os = new ArrayList<Double>();
  //单纯形表计算结果最下一行
  private List<Double> Zs = new ArrayList<Double>();
  //出入基锁定的坐标
  private Integer resultCol;
  private Integer resultRow;

  static {
    config = new ReadProperties("src/main/resources/math/SimplexMethod.properties");
    MAXPARAMS = config.getInt("MaxParams");
    EQUALITY = config.getInt("Equality");
  }

  public static void main(String[] s) {
    SimplexMethod sm = new SimplexMethod();
    sm.run();
  }

  /**
   * 初始化整体数据
   */
  public void init() {
    //添加求解式的数据
    String maxs = config.getString("Max");
    String[] temp = maxs.split(",");
    for (String data : temp) {
      //System.out.println(data);
      Max.add(Double.parseDouble(data));
    }
    //添加约束式的数据
    for (int i = 1; i <= EQUALITY; i++) {
      String buffer = config.getString("E" + i);
      String[] tempList = buffer.split(",");
      Equality e = new Equality();
      for (int j = 0; j < tempList.length; j++) {
        e.getParams().add(Double.parseDouble(tempList[j]));
      }
      e.setResult(config.getDouble("B" + i));
      e.setIndex(config.getInt("I" + i));
      Rows.add(e);
    }
    //填入单纯形表总体数据中去
    for (int i = 0; i < EQUALITY; i++) {
      Equality stRow = Rows.get(i);
      Integer index = stRow.getIndex();
      //装填第一行的数据
      Table table = new Table(Max.get(index - 1), index, stRow.getResult(), stRow.getParams(),
          null);
      Tables.add(table);
    }
    //查看数据是否正确装填
    disp("单纯形表的中心数据 : ", Tables);
//        disp("求解的方程式",Max);
//        disp("约束条件的方程式",Rows);
    //展示方程式
    System.out.println("原题样式 : ");
    showRows();

  }

  //进行表格的运算
  public void run() {
    init();
    Boolean flag = true;
//运算出初始的表格
    //计算最底下一行
    CaculateLastRow();
    //计算右边列
    CaculateRightCol();
    //判断是否达到退出条件
    flag = exitTime(Zs);
    disp("运行状态", Tables);

//迭代的计算直到满足条件
    while (!flag) {
      try {
        Thread.sleep(10);
      } catch (Exception e) {
        e.printStackTrace();
      }
      //算完一轮
      Double fatherNum = Tables.get(resultRow).getRows().get(resultCol);
      List<Table> oldTables = Tables;
      Tables = new ArrayList<Table>();

      //转换出基入基的参数
      List<Double> rowsTemps = oldTables.get(resultRow).getRows();
      for (int k = 0; k < rowsTemps.size(); k++) {
        rowsTemps.set(k, rowsTemps.get(k) / fatherNum);
      }
      Table temp = new Table(Max.get(resultCol), resultCol + 1,
          oldTables.get(resultRow).getB() / fatherNum, rowsTemps, null);
      Tables.add(temp);
      //转换剩余的
      for (int j = 0; j < EQUALITY; j++) {
        if (j != resultRow) {
          rowsTemps = oldTables.get(j).getRows();
          Double motherNum = rowsTemps.get(resultCol);
          Table fatherRow = Tables.get(0);
          for (int k = 0; k < rowsTemps.size(); k++) {
            rowsTemps.set(k, rowsTemps.get(k) - (motherNum * fatherRow.getRows().get(k)));
          }
          Integer tempXb = oldTables.get(j).getXb();
          Table otherTemp = new Table(Max.get(tempXb - 1), tempXb,
              oldTables.get(j).getB() - motherNum * fatherRow.getB(), rowsTemps, null);
          Tables.add(otherTemp);
        }

      }
//            System.out.println("Tables"+Tables.size());
      Zs.clear();
      Os.clear();
      //计算最后一行
      CaculateLastRow();
      //判断是否达到退出条件
      flag = exitTime(Zs);
      if (!flag) {
        CaculateRightCol();
      }
      disp("运行状态", Tables, true);
      disp("底栏", Zs, false);

    }
    //运算完成
    Double result = 0.0;
    Double[] results = new Double[MAXPARAMS];
    for (int i = 0; i < EQUALITY; i++) {
      Table t = Tables.get(i);
      result += t.getCb() * t.getB();
      results[t.getXb() - 1] = t.getB();
    }
    System.out.println("最优目标值是 : " + result);
    String resultstr = "X=(";
    for (Double d : results) {
      if (d != null) {
        resultstr += d + ",";
      } else {
        resultstr += "0,";
      }

    }
    resultstr = resultstr.substring(0, resultstr.length() - 1);
    resultstr += ")";
    System.out.println(resultstr);

  }

  /**
   * 计算最后一行和最右边的一列
   */
  public void CaculateLastRow() {
    for (int i = 0; i < MAXPARAMS; i++) {//循环变量个数
      Double temp = Max.get(i);
      for (int j = 0; j < EQUALITY; j++) {//循环层数
        temp -= Tables.get(j).getRows().get(i) * Tables.get(j).getCb();
      }
      Zs.add(temp);
    }
    disp("最后一行", Zs, false);
    resultCol = MaxList(Zs, true);

  }

  public void CaculateRightCol() {
    //计算右边栏
    for (int i = 0; i < EQUALITY; i++) {
      Double temp = Tables.get(i).getB() / Tables.get(i).getRows().get(resultCol);
      Tables.get(i).setO(temp);
      Os.add(temp);
    }
    //disp("右栏",Os,false);
    resultRow = MaxList(Os, false);
  }

  /**
   * 判断最后一行是否到了最后的计算结果
   *
   * @return true就是算到了最后一步
   */
  public boolean exitTime(List<Double> list) {
    boolean flag = true;
    for (Double b : list) {
      if (b > 0) {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * 求得集合中的极值元素下标
   *
   * @param max true就是最大值
   * @return 极值下标
   */
  public Integer MaxList(List<Double> list, boolean max) {
    Integer index = 0;
    Double temp = list.get(index);
    for (int i = 1; i < list.size(); i++) {
      if (max && temp < list.get(i)) {
        index = i;
        temp = list.get(i);
      }
      if (!max && temp > list.get(i)) {
        index = i;
        temp = list.get(i);
      }
    }
    return index;
  }

  /**
   * 方便展示原始数据
   */
  public void disp(String title, List list) {
    disp(title, list, true);
  }

  public void disp(String title, List list, boolean flag) {
    System.out.println(title);
    for (int i = 0; i < list.size(); i++) {
      if (flag) {
        System.out.println(list.get(i).toString());
      } else {
        System.out.print(list.get(i).toString() + "    ");
      }
    }
    if (!flag) {
      System.out.println();
    }
  }

  /**
   * 展示整体方程式,原题的样子
   */
  public void showRows() {
    StringBuilder MaxRows = new StringBuilder("Max(z)=");
    for (int i = 0; i < MAXPARAMS; i++) {
      if (Max.get(i) != 0) {
        MaxRows.append(Max.get(i)).append(" X").append(i + 1).append(" + ");
      }
    }
    MaxRows = new StringBuilder(MaxRows.substring(0, MaxRows.length() - 2));
    System.out.println("Aim : " + MaxRows);
    for (int i = 0; i < EQUALITY; i++) {
      StringBuilder Row = new StringBuilder();
      List<Double> temp = Rows.get(i).getParams();
      for (int j = 0; j < temp.size(); j++) {
        Double a = temp.get(j);
        if (a != 0) {
          if (a != 1) {
            Row.append(a).append("X").append(j + 1).append(" + ");
          } else {
            Row.append("X").append(j + 1).append(" + ");
          }
        } else {
          Row.append("      ");
        }
      }
      Row = new StringBuilder(Row.substring(0, Row.length() - 2));
      Row.append(" = ").append(Rows.get(i).getResult()).append(" |X").append(Rows.get(i).getIndex())
          .append("|");
      System.out.println("Equality : " + Row);
    }
  }

}

