package com.github.kuangcp.ioc.initdestroy;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class InitDestroyTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/initdestroy/applicationContext.xml";
  }

  @Test
  public void test() {
    ClassPathXmlApplicationContext applicationContext = (ClassPathXmlApplicationContext) context;
    HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
    helloWorld.hello();
    applicationContext.close();
  }
}
