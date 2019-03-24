package com.github.kuangcp.simpleMethod.SimplexMethodQuarter;


import com.github.kuangcp.math.number.Fraction;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Created by Myth on 2017/3/22
 * 使用分数作为基本数据类型来计算
 */
@Data
public class Table {

  private Fraction cb;
  private Integer xb;
  private Fraction bl;
  private List<Fraction> rows;
  private Fraction o;

  public Table(Fraction cb, Integer xb, Fraction bl, List<Fraction> rows, Fraction o) {
    this.cb = cb;
    this.xb = xb;
    this.bl = bl;
    this.o = o;
    this.rows = new ArrayList<>();
    this.rows.addAll(rows);
  }

  @Override
  public String toString() {
    StringBuilder sub = new StringBuilder("[");
    for (Fraction b : rows) {
      sub.append(b.toString()).append(",");
    }
    sub.append("]");
    return "Table{" +
        "cb=" + cb +
        ", xb=" + xb +
        ", bl=" + bl +
        ", rows=" + sub +
        ", o=" + o +
        '}';
  }
}
