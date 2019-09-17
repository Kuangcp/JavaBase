package com.github.kuangcp.proxy.salary.proxy;

import org.junit.Test;

public class ProxyTest {

  @Test
  public void test() {
    Logger logger = new Logger();
    Privilege privilege = new Privilege();
    privilege.setAccess("admin");
    Security security = new Security();
    SalaryManager target = new SalaryManagerImpl();
    SalaryManagerProxy proxy = new SalaryManagerProxy(logger, security, privilege, target);
    proxy.showSalary();
  }
}
