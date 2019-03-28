package com.github.kuangcp.proxy.salary.jdkproxy.aspects;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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
	
	private List<Interceptor> interceptorList;
	
	private Object target;
	
	public SalaryInterceptor(Logger logger,Security security,Privilege privilege,Object target,List<Interceptor> interceptorList){
		this.logger = logger;
		this.security = security;
		this.privilege = privilege;
		this.target = target;
		this.interceptorList = interceptorList;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		/**
		 * 执行所有的切面中的通知
		 */
		for(Interceptor interceptor:interceptorList){
			interceptor.interceptor();
		}
		method.invoke(this.target, args);
		return null;
	}

}
