package com.github.kuangcp.jdbc.jdbc;

import com.github.kuangcp.util.SpringHelper;
import java.util.List;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void testSave() {
    PersonDao personDao = (PersonDao) context.getBean("personDao");
    personDao.savePerson();
  }

  @Test
  public void testQuery() {
    PersonDao personDao = (PersonDao) context.getBean("personDao");
    List<Person> personList = personDao.getPersons();
    System.out.println(personList.size());
  }
}
