package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonService personService = (PersonService) context.getBean("personService");
    personService.savePerson();
  }
}
