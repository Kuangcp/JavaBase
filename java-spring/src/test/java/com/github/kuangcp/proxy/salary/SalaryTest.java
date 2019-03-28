package com.github.kuangcp.proxy.salary;

import org.junit.Test;

public class SalaryTest {
	@Test
	public void test(){
		Logger logger = new Logger();
		Privilege privilege = new Privilege();
		Security security = new Security();
		privilege.setAccess("admin");
		SalaryManager salaryManager = new SalaryManager(logger, security, privilege);
		salaryManager.showSalary();
	}
}
