package com.github.kuangcp.ioc.alias;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class AliasTest extends SpringHelper {

  /**
   * 无论这两个类之间有什么继承关系，静态代码块比方法先执行
   * alia是对一个bean取别名
   */
  static {
    path = "cn/itcast/spring0909/alias/applicationContext.xml";
  }

  @Test
  public void test() {
    HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
    HelloWorld helloWorld2 = (HelloWorld) context.getBean("王二麻子");
//		HelloWorld helloWorld = (HelloWorld)context.getBean("helloWorld");
//		HelloWorld helloWorld = (HelloWorld)context.getBean("helloWorld");
    helloWorld2.hello();
  }
}
