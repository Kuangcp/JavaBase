package com.github.kuangcp.nesting;

import lombok.Data;

/**
 * @author kuangcp on 19-1-10-下午2:29
 */
@Data
public class HumanVO implements JsonVO<String> {

  private String id;
  private int age;

  @Override
  public String getId() {
    return id;
  }
}
