package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.base.Transaction;
import java.lang.reflect.Proxy;
import org.junit.Test;

public class JDKProxyTest {

  @Test
  public void testMain() {
    //增强
    Transaction transaction = new Transaction();
    //切点
    PersonDao target = new PersonDaoImpl();
    //拦截器 重写了invoke方法，将需要增强的对象target传入
    PersonDaoInterceptor interceptor = new PersonDaoInterceptor(transaction, target);
    //使用jdk的代理对象来实例化，
    //参数解释：切点类装载器，切点类接口，实现了接口的（将增强织入切点的）实例
    PersonDao proxy = (PersonDao) Proxy
        .newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
            interceptor);
    proxy.deletePerson();
  }
}
