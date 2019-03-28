package com.github.kuangcp.proxy.salary.aop.xml;

import java.lang.reflect.Method;
import org.junit.Test;

public class Person {

  private Long pid;
  private String pname;

  public Long getPid() throws Exception {
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

  @Test
  public void test() throws Exception {
    Method method = Person.class.getMethod("getPid");
    System.out.println(method.toString());
  }
}
