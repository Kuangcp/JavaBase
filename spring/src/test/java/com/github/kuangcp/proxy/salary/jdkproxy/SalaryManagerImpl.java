package com.github.kuangcp.proxy.salary.jdkproxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalaryManagerImpl implements SalaryManager {

  @Override
  public void showSalary() {
    log.info("reading salary detail");
  }
}
