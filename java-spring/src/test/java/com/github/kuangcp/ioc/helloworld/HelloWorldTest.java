package com.github.kuangcp.ioc.helloworld;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloWorldTest {
	@Test
	public void test(){
		/*
		 * 1、启动spring容器
		 * 2、从spring容器中把helloWorld拿出来
		 * 3、对象.方法
		 */
		//启动spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("cn/itcast/spring0909/helloworld/applicationContext.xml");
		HelloWorld helloWorld = (HelloWorld)context.getBean("helloWorld");
		helloWorld.hello();
	}
}
