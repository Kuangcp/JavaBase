package com.github.kuangcp.proxy.salary.proxy;

/**
 * 1、导入日志、安全性框架、权限 2、目标对象 3、在代理对象的方法中调用上述的方法
 * 
 * @author Administrator
 * 
 */
public class SalaryManagerProxy implements SalaryManager {
	private Logger logger;
	private Security security;
	private Privilege privilege;
	private SalaryManager target;

	public SalaryManagerProxy(Logger logger, Security security,
			Privilege privilege, SalaryManager target) {
		this.logger = logger;
		this.security = security;
		this.privilege = privilege;
		this.target = target;
	}

	@Override
	public void showSalary() {
		this.logger.logging();// 启动日志
		this.security.security();// 安全性框架
		if ("admin".equals(this.privilege.getAccess())) {
			this.target.showSalary();
		} else {
			System.out.println("您没有权限");
		}
	}

}
