package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.common.Permission;
import com.github.kuangcp.proxy.dao.common.Transaction;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JDKProxyTest {

  @Test
  public void testTransaction() {
    // 被代理对象
    PersonDao target = new PersonDaoImpl();
    PersonDao proxy = getTransactionProxy(target);
    proxy.getPerson();
    proxy.savePerson();
    proxy.deletePerson();
  }

  @Test
  public void testPermission() {
    // 被代理对象
    PersonDao target = new PersonDaoImpl();
    PersonDao proxy = getPermissionProxy(target);
    proxy.getPerson();
    proxy.savePerson();
    proxy.deletePerson();
  }

  /**
   * 嵌套代理，权限 -> 事务 -> 被代理对象
   */
  @Test
  public void testMix() {
    // 被代理对象
    PersonDao target = new PersonDaoImpl();
    PersonDao proxy = getTransactionProxy(target);
    PersonDao permissionProxy = getPermissionProxy(proxy);

    System.out.println();
    permissionProxy.getPerson();
    System.out.println();
    permissionProxy.savePerson();
    System.out.println();
    permissionProxy.deletePerson();
  }

  private PersonDao getTransactionProxy(PersonDao target) {
    // 代理逻辑
    Transaction transaction = new Transaction();

    // 重写了invoke方法，将需要增强的对象target传入
    TransactionInterceptor interceptor = new TransactionInterceptor(transaction, target);

    //使用jdk的代理对象来实例化，
    //参数解释：切点类装载器，切点类接口，实现了接口的（将增强织入切点的）实例
    Class<?>[] interfaces = target.getClass().getInterfaces();
    log.debug(": interfaces={}", Arrays.toString(interfaces));

    return (PersonDao) Proxy
        .newProxyInstance(target.getClass().getClassLoader(), interfaces, interceptor);
  }

  private PersonDao getPermissionProxy(PersonDao target) {
    // 代理逻辑
    Permission permission = new Permission();

    // 重写了invoke方法，将需要增强的对象target传入
    PermissionInterceptor interceptor = new PermissionInterceptor(permission, target);

    //使用jdk的代理对象来实例化，
    //参数解释：切点类装载器，切点类接口，实现了接口的（将增强织入切点的）实例
    Class<?>[] interfaces = target.getClass().getInterfaces();
    log.debug(": interfaces={}", Arrays.toString(interfaces));

    return (PersonDao) Proxy
        .newProxyInstance(target.getClass().getClassLoader(), interfaces, interceptor);
  }
}
