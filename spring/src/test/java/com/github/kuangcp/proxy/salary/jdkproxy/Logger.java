package com.github.kuangcp.proxy.salary.jdkproxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger implements Interceptor {

  @Override
  public void interceptor() {
    log.info("logging");
  }
}
