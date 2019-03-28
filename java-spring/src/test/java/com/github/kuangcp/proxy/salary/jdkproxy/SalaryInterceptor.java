package com.github.kuangcp.proxy.salary.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 1、把日志、安全性框架、权限导入进去
 * 2、把目标类导入进去
 * 3、上述两类通过构造函数赋值
 * @author Administrator
 *
 */
public class SalaryInterceptor implements InvocationHandler{
	
	private Logger logger;
	private Security security;
	private Privilege privilege;
	
	private Object target;
	
	public SalaryInterceptor(Logger logger,Security security,Privilege privilege,Object target){
		this.logger = logger;
		this.security = security;
		this.privilege = privilege;
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("aaaaaa");
		this.logger.logging();
		this.security.security();
		if("admin".equals(this.privilege.getAccess())){
			//调用目标类的目标方法
			method.invoke(this.target, args);
		}else{
			System.out.println("您没有该权限");
		}
		System.out.println("bbbbbb");
		return null;
	}

}
