package com.github.kuangcp.simpleMethod.SimplexMethodQuarter;


import com.github.kuangcp.math.number.Fraction;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Created by Myth on 2017/3/22
 * 约束式的对象形式 分数数据类型
 */
@Data
public class Equality {

  Integer index;
  List<Fraction> params;
  Fraction result;

  public Equality() {
    params = new ArrayList<>();
  }

  public Equality(List<Fraction> params, Fraction result) {
    this.params = params;
    this.result = result;
  }

  public Equality(Integer index, List<Fraction> params, Fraction result) {
    this.index = index;
    this.params = params;
    this.result = result;
  }

  @Override
  public String toString() {
    StringBuilder sub = new StringBuilder("[");
    for (Fraction b : params) {
      sub.append(b.toString()).append(",");
    }
    sub.append("]");
    return "Equality{" +
        "index=" + index +
        ", params=" + sub +
        ", result=" + result +
        '}';
  }
}
