package com.github.kuangcp.instantiation;

import java.io.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试 实例化对象和构造器之间的关系
 *
 * @author kuangcp on 3/9/19-5:46 PM
 */
@Data
@Slf4j
class InstantiationAndConstructor implements Serializable, Cloneable {

  private String name;

  static {
    log.info("invoke static init block");
  }

  {
    log.info("invoke init block");
  }

  public InstantiationAndConstructor() {
    log.warn("invoke empty constructor");
  }

  public InstantiationAndConstructor(String name) {
    this.name = name;
    log.warn("invoke constructor(name): name={}", name);
  }

  @Override
  protected InstantiationAndConstructor clone() throws CloneNotSupportedException {
    return (InstantiationAndConstructor) super.clone();
  }
}
