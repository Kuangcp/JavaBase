package com.github.kuangcp.aop.exception;

import com.github.kuangcp.aop.exception.action.PersonAction;
import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class ExceptionTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "aop/exception/applicationContext-exception.xml";
  }

  @Test
  public void test() throws Exception {
    PersonAction personAction = (PersonAction) context.getBean("personAction");
    personAction.savePerson();
  }
}
