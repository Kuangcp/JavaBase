package com.github.kuangcp.aop.annotation;

import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;

/**
 * @author Administrator
 */
public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "aop/annotation/applicationContext.xml";
  }

  @Test
  public void testAnnotation() {
    PersonDaoImpl personDao = (PersonDaoImpl) context.getBean("personDao");
    personDao.getPerson();
  }
}
