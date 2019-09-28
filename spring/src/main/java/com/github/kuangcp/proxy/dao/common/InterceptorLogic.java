package com.github.kuangcp.proxy.dao.common;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理部分公共逻辑
 * 思考将 Transaction 和 Permission 抽象一层就成为了 Aspect AOP 这套设计了
 *
 * @author https://github.com/kuangcp on 2019-09-29 01:20
 */
@Slf4j
public class InterceptorLogic {

  public static Object transactionLogic(Transaction transaction, Object target,
      Method method, Object[] args) throws Throwable {
    Object result;
    //对指定的方法增强
    if (isNeedTransaction(method.getName())) {
      transaction.beginTransaction();
      try {
        //调用目标类的目标方法
        result = method.invoke(target, args);
        transaction.commit();
      } catch (Exception e) {
        log.error("", e);
        transaction.rollback();
        throw e;
      }
    } else {
      //调用目标类的目标方法
      result = method.invoke(target, args);
    }
    return result;
  }

  public static Object permissionLogic(Permission permission, Object target,
      Method method, Object[] args) throws Throwable {
    boolean hasPermission = permission.hasPermission(target);
    if (!hasPermission) {
      throw new RuntimeException("forbid invoke this method: " + method.getName());
    }
    return method.invoke(target, args);
  }

  private static boolean isNeedTransaction(String methodName) {
    return "savePerson".equals(methodName)
        || "updatePerson".equals(methodName)
        || "deletePerson".equals(methodName);
  }
}
