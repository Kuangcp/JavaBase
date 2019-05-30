package com.github.kuangcp.mvc;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/mvc/spring/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonAction personAction = (PersonAction) context.getBean("personAction");
    personAction.savePerson();
  }
}
