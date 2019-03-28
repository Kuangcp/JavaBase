package com.github.kuangcp.proxy.salary.jdkproxy;

import java.lang.reflect.Proxy;
import org.junit.Test;

/**
 * 1、拦截器的作用究竟是什么？
 *     *  给目标类及其他的类赋值
 *     *  拦截器中的invoke方法的内容就是代理对象的方法的内容
 * 2、代理对象的方法体是什么？
 *     就是拦截器中invoke方法中的内容
 * 3、在拦截器中有一个方法为invoke方法的第二个参数method,该参数是什么时候传递进去的
 *     代理对象调用方法的时候，就进入了拦截器中invoke方法中
 * 4、动态代理模式到底解决了什么问题
 * @author Administrator
 */
public class ProxyTest {
	@Test
	public void test(){
		Logger logger = new Logger();
		Privilege privilege = new Privilege();
		privilege.setAccess("aaaa");
		Security security = new Security();
		SalaryManager target = new SalaryManagerImpl();
		SalaryInterceptor interceptor = new SalaryInterceptor(logger, security, privilege, target);
		/**
		 * 1、目标类的类加载器
		 * 2、目标类的所有的接口
		 * 3、拦截器
		 */
		 SalaryManager proxy = (SalaryManager) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), interceptor);
		 proxy.showSalary();//代理对象的代理方法
	}
}
