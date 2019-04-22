package com.github.kuangcp.proxy.dao.base;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Transaction {

  public void beginTransaction() {
    log.info("begin transaction");
  }

  public void commit() {
    log.info("commit");
  }
}
