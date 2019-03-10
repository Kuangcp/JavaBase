package com.github.kuangcp.instantiation;

import java.io.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 3/9/19-5:46 PM
 */
@Data
@Slf4j
public class TargetObject implements Serializable, Cloneable {

  private String name;

  public TargetObject() {
    log.info("invoke empty constructor");
  }

  public TargetObject(String name) {
    this.name = name;
    log.info("invoke constructor: name={}", name);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "TargetObject{" +
        "name='" + name + '\'' +
        '}';
  }
}
