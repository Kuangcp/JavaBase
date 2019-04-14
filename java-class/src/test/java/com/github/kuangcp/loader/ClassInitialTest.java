package com.github.kuangcp.loader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-15 12:08 AM
 */
@Slf4j
public class ClassInitialTest {

  public static void main(String[] args) throws ClassNotFoundException {
    String path = "com.github.kuangcp.loader.InitialTest";
    Class<InitialTest> clazz = InitialTest.class;
    log.debug("InitialTest.class");

    Thread.currentThread().getContextClassLoader().loadClass(path);
    log.debug("classLoader.loadClass");

    Class.forName(path);
    log.debug("Class.forName");
  }
}
@Slf4j
class InitialTest {

  static {
    log.info("init class");
  }
}
