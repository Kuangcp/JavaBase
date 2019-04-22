package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.base.Transaction;
import org.junit.Test;

public class CGlibProxyTest {

  @Test
  public void testMain() {
    Transaction transaction = new Transaction();
    PersonDaoImpl target = new PersonDaoImpl();
    PersonDaoInterceptor interceptor = new PersonDaoInterceptor(transaction, target);
    PersonDaoImpl proxy = (PersonDaoImpl) interceptor.createProxy();
    proxy.updatePerson();
  }
}
