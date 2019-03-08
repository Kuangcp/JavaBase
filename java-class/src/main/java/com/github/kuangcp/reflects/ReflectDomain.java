package com.github.kuangcp.reflects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:48
 * 反射操作的对象
 *
 * @author kuangcp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ReflectDomain {

  private static final String uid = "static final";
  private final String id = "final";

  private String name;
  private Long age;
}
