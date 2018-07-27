package com.github.kuangcp.simpleMethod.number;

/**
 * Created by Myth on 2017/3/21
 * 将浮点数转换成分数，并提供相关操作方法 貌似是不可行的
 * 最多是给定一个0.3（3）指定循环部分可以求出分数
 * 使用两个字符串分别表示分子和分母来计算,提供加减乘除以及化简的方法
 *
 * 怎么这么笨，不直接用整数来做分子分母，还用字符串，明明要进行计算的、又要重新写了
 *
 * 使用Integer就确定了是最长11位的整数长度
 * 改成数值了，还使用符号的标记是为了啥，真是可以了。
 */
public class Quarter {

  private Integer numerator;//分子
  private Integer denominator;//分母

  public Quarter() {
  }

  /**
   * 使用构造器来进行对象的复制
   *
   * @param quarter 原对象
   */
  public Quarter(Quarter quarter) {
    this.numerator = quarter.getNumerator();
    this.denominator = quarter.getDenominator();
  }

  public Quarter(Integer numerator, Integer denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  /**
   * 整数构造器 分母为1
   *
   * @param numerator 整数
   */
  public Quarter(Integer numerator) {
    this.numerator = numerator;
    this.denominator = 1;
  }

  /**
   * 使用字符串来构造一个分数 例如 -12.3434
   *
   * @param num 字符串
   */
  public Quarter(String num) {
    Integer N, D, minus;
    //负数
    String[] data = num.split("\\.");

    if (num.startsWith("-")) {
      minus = -1;
      if (data.length == 2) {//含有小数点
        N = Integer.parseInt(data[0].substring(1));//整数部分
        D = Integer.parseInt(data[1]);
        Double dd = Math.pow(10.0, data[1].length());
        Integer temp = dd.intValue();
        this.setNumerator(minus * N * temp + D);
        this.setDenominator(temp);
      } else {
        N = Integer.parseInt(num.substring(1));
        this.setNumerator(minus * N);
        this.setDenominator(1);
      }
    } else {//正数
      if (data.length == 2) {
        N = Integer.parseInt(data[0]);
        D = Integer.parseInt(data[1]);
        Double dd = Math.pow(10.0, data[1].length());
        Integer temp = dd.intValue();
        this.setNumerator(N * temp + D);
        this.setDenominator(temp);
      } else {
        this.setNumerator(Integer.parseInt(num));
        this.setDenominator(1);
      }
    }
    this.simple();

  }

  /**
   * 加法运算
   *
   * @param other 加数
   * @return 分数对象
   */
  public Quarter plus(Quarter other) {
    this.simple();
    other.simple();
    Quarter result = new Quarter();

    Integer one_D = this.getDenominator();
    Integer one_N = this.getNumerator();
    Integer other_D = other.getDenominator();
    Integer other_N = other.getNumerator();

    //分母相同
    if (this.getDenominator().equals(other.getDenominator())) {
      Integer temp = one_N + other_N;
//                System.out.println("分子运算结果"+temp);
      result.setNumerator(temp);
      result.setDenominator(this.getDenominator());
    } else {
      Integer temp = one_N * other_D + one_D * other_N;
//                System.out.println("分子运算结果"+temp);
      result.setNumerator(temp);
      result.setDenominator(one_D * other_D);
//                System.out.println("结果"+result.toString());
    }
    return result.simple();
  }

  /**
   * 乘法运算
   *
   * @param other 乘数(分数对象)
   * @return Quarter 分数对象
   */
  public Quarter multi(Quarter other) {
    Quarter result = new Quarter();
    //排除特殊情况
    if (this.getNumerator() == 0 || other.getNumerator() == 0) {
      result = new Quarter(0);
      return result;
    }
    if (this.getDenominator() == 0 || other.getDenominator() == 0) {
//            throw new Exception("使用非数进行了乘除运算");
      //为了计算不中断
      result.setNumerator(1);
      result.setDenominator(0);
      return result;
    }
    Integer one_D = this.getDenominator();
    Integer one_N = this.getNumerator();
    Integer other_D = other.getDenominator();
    Integer other_N = other.getNumerator();
    result.setNumerator((one_N * other_N));
    result.setDenominator((one_D * other_D));
    return result.simple();
  }

  /**
   * 乘法运算
   *
   * @param other 乘数(整数)
   * @return Quarter 分数对象
   */
  public Quarter multi(Integer other) {
    Quarter result = new Quarter();
    result.setNumerator((this.getNumerator() * other));
    result.setDenominator(this.getDenominator());
    return result.simple();
  }

  /**
   * 减法操作
   *
   * @param other 减数
   * @return Quarter 分数对象
   */
  public Quarter reduce(Quarter other) {
    Quarter quarter = new Quarter(other);
    //变号 相加
    quarter.setNumerator(-1 * quarter.getNumerator());
    return plus(quarter);
  }

  /**
   * 除法运算
   *
   * @param other 除数
   * @return Quarter 分数对象
   */
  public Quarter divide(Quarter other) {
    Quarter quarter = new Quarter(other);
    if (this.getNumerator() == 0) {
      quarter.setNumerator(0);
      quarter.setDenominator(1);
      return quarter;
    }
    if (this.getDenominator() == 0 || other.getDenominator() == 0 || other.getNumerator() == 0) {
//            throw new Exception("使用非数进行了乘除运算");
      //为了计算不中断，只好这样
      quarter.setNumerator(1);
      quarter.setDenominator(0);
      return quarter;
    }
    //交换分子分母
    Integer temp = other.getDenominator();
    quarter.setDenominator(other.getNumerator());
    quarter.setNumerator(temp);
    return multi(quarter);
  }

  /**
   * 是否大于另一个数
   *
   * @param other 比较的数
   */
  public boolean bigger(Quarter other) {
    return this.reduce(other).upZero();
  }

  /**
   * 化简函数 使用辗转相除来求最小公约数进行化简
   *
   * @return Quarter 分数对象
   */
  public Quarter simple() {
    Integer D, N;
    D = Math.abs(this.getDenominator());
    N = Math.abs(this.getNumerator());
    if (D == 0) {
      return this;
    }
    Integer temp;
    //辗转相除来计算公约数
    while (D != 0) {
      temp = N % D;
      N = D;
      D = temp;
    }
    if (N > 0) {
      Integer tempD = 1;
      if (this.getDenominator() < 0) {
        tempD = -1;
      }
      this.setDenominator(tempD * this.getDenominator() / N);
      this.setNumerator(tempD * this.getNumerator() / N);
    }
    return this;
  }

  /**
   * 判断是否大于 0
   */
  public boolean upZero() {
    this.simple();
    return this.getDenominator() != 0 && !this.isZero() && this.getNumerator() > 0;
  }

  /**
   * 判断该分数是否是零
   */
  public boolean isZero() {
    boolean flag = false;
    if (this.getNumerator() == 0) {
      flag = true;
    }
    if (this.getDenominator() == 0) {
      flag = false;
    }
    return flag;
  }

  /**
   * 判断是否为1
   */
  public boolean isOne() {
    return this.getNumerator() == 1 && this.getDenominator() == 1;
  }

  /**
   * 判断是否为 非数
   */
  public boolean isInfinity() {
    return this.getDenominator() == 0;
  }

  public Integer getNumerator() {
    return numerator;
  }

  public void setNumerator(Integer numerator) {
    this.numerator = numerator;
  }

  public Integer getDenominator() {
    return denominator;
  }

  public void setDenominator(Integer denominator) {
    this.denominator = denominator;
  }

  @Override
  public String toString() {
    if (1 == denominator) {
      return numerator + " ";
    } else if (0 == denominator) {
      return "Infinity";
    } else {
      return "" + numerator + "/" + denominator + " ";
    }
  }
}