package com.github.kuangcp.simpleMethod.SimplexMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/21 0021
 * 单纯形表的总体结构中的行对象
 */
public class Table {

  private Double Cb;
  private Integer Xb;
  private Double b;
  private List<Double> rows;
  private Double O;

  public Table() {
  }

  public Table(Double cb, Integer xb, Double b, List<Double> row, Double o) {
    Cb = cb;
    Xb = xb;
    this.b = b;
    O = o;
    rows = new ArrayList<>();
    rows.addAll(row);
  }

  public Double getCb() {
    return Cb;
  }

  public void setCb(Double cb) {
    Cb = cb;
  }

  public Integer getXb() {
    return Xb;
  }

  public void setXb(Integer xb) {
    Xb = xb;
  }

  public Double getB() {
    return b;
  }

  public void setB(Double b) {
    this.b = b;
  }

  public List<Double> getRows() {
    return rows;
  }

  public void setRows(List<Double> rows) {
    this.rows = rows;
  }

  public Double getO() {
    return O;
  }

  public void setO(Double o) {
    O = o;
  }

  @Override
  public String toString() {
    StringBuilder sub = new StringBuilder("[");
    for (Double b : rows) {
      sub.append(b).append(",");
    }
    sub.append("]");
    return "Table{" +
        "Cb=" + Cb +
        ", Xb=" + Xb +
        ", b=" + b +
        ", rows=" + sub +
        ", O=" + O +
        '}';
  }
}
