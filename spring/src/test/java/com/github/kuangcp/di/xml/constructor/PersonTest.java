package com.github.kuangcp.di.xml.constructor;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    Person person = (Person) context.getBean("person");
    person.getStudent().say();
  }
}
