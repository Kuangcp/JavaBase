package com.github.kuangcp.aop.xml.salary;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class SalaryTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/aop/xml/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    SalaryManager salaryManager = (SalaryManager) context.getBean("salaryManager");
    salaryManager.showSalary();
  }
}
