package com.github.kuangcp.aop.annotation;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

/**
 * @author Administrator
 */
public class PersonTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/aop/annotation/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonDaoImpl personDao = (PersonDaoImpl) context.getBean("personDao");
    personDao.getPerson();
  }
}
