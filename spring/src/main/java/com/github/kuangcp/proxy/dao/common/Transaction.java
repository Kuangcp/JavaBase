package com.github.kuangcp.proxy.dao.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Transaction {

  void beginTransaction() {
    log.info("begin transaction");
  }

  void commit() {
    log.info("commit");
  }

  void rollback() {
    log.info("rollback");
  }
}
