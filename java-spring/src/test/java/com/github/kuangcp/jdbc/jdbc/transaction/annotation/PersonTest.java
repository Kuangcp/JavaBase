package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/jdbc/transaction/annotation/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonService personService = (PersonService) context.getBean("personService");
    personService.savePerson();
  }
}
