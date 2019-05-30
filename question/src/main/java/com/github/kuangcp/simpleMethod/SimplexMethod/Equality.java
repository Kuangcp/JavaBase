package com.github.kuangcp.simpleMethod.SimplexMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/20 0020
 * 约束式的对象形式
 */
public class Equality {

  //式子的代表性参数X的下标
  Integer index;
  List<Double> params;
  Double result;

  public Equality() {
    params = new ArrayList<Double>();
  }

  public Equality(List<Double> params, Double result) {
    this.params = params;
    this.result = result;
  }

  public List<Double> getParams() {
    return params;
  }

  public void setParams(List<Double> params) {
    this.params = params;
  }

  public Double getResult() {
    return result;
  }

  public void setResult(Double result) {
    this.result = result;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  @Override
  public String toString() {
    StringBuilder sub = new StringBuilder("[");
    for (Double b : params) {
      sub.append(b).append(",");
    }
    sub.append("]");
    return "Equality{" +
        "params=" + sub +
        ", result=" + result +
        '}';
  }
}
