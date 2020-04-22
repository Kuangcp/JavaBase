package com.github.kuangcp.mvc.annotation;

import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonAction personAction = (PersonAction) context.getBean("personAction");
    personAction.savePerson();
  }
}
