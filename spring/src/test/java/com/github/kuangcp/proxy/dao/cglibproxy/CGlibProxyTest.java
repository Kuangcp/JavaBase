package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.common.Transaction;
import org.junit.Test;

public class CGlibProxyTest {

  @Test
  public void testTransaction() {
    Transaction transaction = new Transaction();
    PersonDaoImpl target = new PersonDaoImpl();
    TransactionInterceptor interceptor = new TransactionInterceptor(transaction, target);

    PersonDaoImpl proxy = (PersonDaoImpl) interceptor.createProxy();
    proxy.updatePerson();
    System.out.println();
    proxy.getPerson();
    System.out.println();
    proxy.savePerson();
    System.out.println();
    proxy.deletePerson();
  }
}
