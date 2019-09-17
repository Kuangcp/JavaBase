package com.github.kuangcp.util;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class SpringHelper {

  public static ApplicationContext context;

  public abstract String getXmlPath();

  @Before
  public void startSpring() {
    context = new ClassPathXmlApplicationContext(getXmlPath());
  }
}
