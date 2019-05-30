package com.github.kuangcp.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp on 3/6/19-3:14 PM
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Human {

  private String name;

  public Human() {
  }

  public Human(String name) {
    this.name = name;
  }

}
