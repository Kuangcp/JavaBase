package com.github.kuangcp.ioc.initdestroy;

import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class InitDestroyTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    ClassPathXmlApplicationContext applicationContext = (ClassPathXmlApplicationContext) context;
    HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
    helloWorld.hello();
    applicationContext.close();
  }
}
