package com.github.kuangcp.util;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHelper {

  public static ApplicationContext context;
  public static String path;

  @Before
  public void startSpring() {
    context = new ClassPathXmlApplicationContext(path);
  }
}
