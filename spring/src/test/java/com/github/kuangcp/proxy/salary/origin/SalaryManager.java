package com.github.kuangcp.proxy.salary.origin;

public class SalaryManager {

  private Logger logger;
  private Security security;
  private Privilege privilege;

  public SalaryManager(Logger logger, Security security, Privilege privilege) {
    this.logger = logger;
    this.security = security;
    this.privilege = privilege;
  }

  public void showSalary() {
    this.logger.logging();
    this.security.security();
    if ("admin".equals(this.privilege.getAccess())) {
      System.out.println("正在查看工资：涨了2W");
    } else {
      System.out.println("您没有该权限");
    }
  }
}
