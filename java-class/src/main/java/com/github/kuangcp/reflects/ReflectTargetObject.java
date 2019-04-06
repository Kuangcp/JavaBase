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
  public static final Integer staticFinalInteger = 1;

  // String 以及基本类型都是改不了的, 编译后被优化成了字面量, 反编译下这个类生成的字节码就知道了
  //  public String getFinalString() {
  //    this.getClass();
  //    return "final";
  //  }

  //  public int getFinalInt() {
  //    this.getClass();
  //    return 1;
  //  }

  // 初始化方式改成 new String("100"); 或者 "100"!=null?"100":"";
  // 这种需要运算的方式, 就不会被优化成字节码里的字面量
  public final String finalString = "final";
  public final int finalInt = 1;

  // 能改动
  public final Integer finalInteger = 1;

  private String name;
  private int num = 1;

  public ReflectTargetObject(String name) {
    this.name = name;
  }
}
