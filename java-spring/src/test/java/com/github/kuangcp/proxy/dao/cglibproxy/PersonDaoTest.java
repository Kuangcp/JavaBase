package com.github.kuangcp.proxy.dao.cglibproxy;

import org.junit.Test;

public class PersonDaoTest {

  @Test
  public void test() {
    Transaction transaction = new Transaction();
    PersonDaoImpl target = new PersonDaoImpl();
    PersonDaoInteceptor inteceptor = new PersonDaoInteceptor(transaction, target);
    PersonDaoImpl proxy = (PersonDaoImpl) inteceptor.createProxy();
    proxy.updatePerson();
  }
}
