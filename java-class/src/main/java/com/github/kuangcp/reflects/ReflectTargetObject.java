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

  public static final String uid = "staticFinal";
  public final String id = "final";
  public final int no = 1;

  public static String type = "type";

  private String name;
  private int num = 2;

  public ReflectTargetObject(String name) {
    this.name = name;
  }
}
