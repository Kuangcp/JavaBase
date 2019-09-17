package com.github.kuangcp.aop.annotation;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

/**
 * @author Administrator
 */
public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonDaoImpl personDao = (PersonDaoImpl) context.getBean("personDao");
    personDao.getPerson();
  }
}
