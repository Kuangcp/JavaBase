package com.github.kuangcp.reflects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:48
 *
 * @author kuangcp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ReflectTargetObject {

  public static final String staticFinalString = "staticFinal";

  // 改不动
  public final String finalString = "final";
  // 能改动
  public final int finalInt = 1;
  public final Integer finalInteger = 2;

  public static String type = "type";

  private String name;
  private int num = 2;

  public ReflectTargetObject(String name) {
    this.name = name;
  }
}
