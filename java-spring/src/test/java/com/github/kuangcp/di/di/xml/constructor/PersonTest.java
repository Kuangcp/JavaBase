package com.github.kuangcp.di.di.xml.constructor;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/di/xml/constructor/applicationContext.xml";
  }

  @Test
  public void test() {
    Person person = (Person) context.getBean("person");
    person.getStudent().say();
  }
}
