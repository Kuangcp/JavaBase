package com.github.kuangcp.exception;

import com.github.kuangcp.exception.service.StudentServiceImpl;
import org.junit.Test;

public class ExceptionTest {

  @Test
  public void test() {
    ServiceMapping serviceMapping = new ServiceMapping();
    serviceMapping.setServiceClass(StudentServiceImpl.class.getName());
    serviceMapping.setMethod("savePerson");
    ServiceInvocation.execution(serviceMapping);
  }
}
