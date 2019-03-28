package com.github.kuangcp.extend;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class PersonTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/extend/applicationContext.xml";
  }

  @Test
  public void test() {
    Student student = (Student) context.getBean("student");
    student.say();
  }
}
