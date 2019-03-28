package com.github.kuangcp.exception;

import org.junit.Test;

public class ExceptionTest {

  @Test
  public void test() {
    ServiceMapping serviceMapping = new ServiceMapping();
    serviceMapping.setServiceClass("cn.itcast.exception.service.StudentServiceImpl");
    serviceMapping.setMethod("savePerson");
    ServiceInvocation.execution(serviceMapping);
  }
}
