package com.github.kuangcp.proxy.dao.jdkproxy;

public class Transaction {

  public void beginTransaction() {
    System.out.println("begin transaction");
  }

  public void commit() {
    System.out.println("commit");
  }
}
