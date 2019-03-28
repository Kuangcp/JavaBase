package com.github.kuangcp.ioc.createobject.when;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class WhenTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/createobject/when/applicationContext.xml";
  }

  @Test
  public void test() {
    HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
    Person person = (Person) context.getBean("person");
    helloWorld.hello();
    person.say();
  }
}
