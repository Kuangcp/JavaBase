package com.github.kuangcp.extend;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class PersonTest extends SpringHelper {


  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    Student student = (Student) context.getBean("student");
    student.say();
  }
}
