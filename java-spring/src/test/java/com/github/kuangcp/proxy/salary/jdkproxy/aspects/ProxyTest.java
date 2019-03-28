package com.github.kuangcp.proxy.salary.jdkproxy.aspects;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ProxyTest {
	@Test
	public void test(){
		Logger logger = new Logger();
		Privilege privilege = new Privilege();
		Security security = new Security();
		List<Interceptor> interceptorList = new ArrayList<Interceptor>();
		interceptorList.add(logger);
		interceptorList.add(privilege);
		interceptorList.add(security);
		SalaryManager target = new SalaryManagerImpl();
		SalaryInterceptor interceptor = new SalaryInterceptor(logger, security, privilege, target,interceptorList);
		/**
		 * 1、目标类的类加载器
		 * 2、目标类的所有的接口
		 * 3、拦截器
		 */
		 SalaryManager proxy = (SalaryManager) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), interceptor);
		 proxy.showSalary();//代理对象的代理方法
	}
}
