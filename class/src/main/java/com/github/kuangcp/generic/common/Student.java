package com.github.kuangcp.generic.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp on 3/6/19-3:14 PM
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class Student extends Human {

  private String school;

  public Student() {
  }

  public Student(String school) {
    this.school = school;
  }

}
