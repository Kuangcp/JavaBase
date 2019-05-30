package com.github.kuangcp.hibernate.annotation;

import java.io.Serializable;

public class Person implements Serializable {

  private Long pid;
  private String pname;
  private String psex;

  public String getPsex() {
    return psex;
  }

  public void setPsex(String psex) {
    this.psex = psex;
  }

  public Long getPid() {
    return pid;
  }

  public void setPid(Long pid) {
    this.pid = pid;
  }

  public String getPname() {
    return pname;
  }

  public void setPname(String pname) {
    this.pname = pname;
  }
}
