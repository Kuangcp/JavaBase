package com.github.kuangcp.proxy.dao.base;

/**
 * @author kuangcp on 2019-04-22 6:05 PM
 */
public interface CustomInterceptor {

  default boolean isNeedTransaction(String methodName) {
    return "savePerson".equals(methodName) || "updatePerson".equals(methodName) ||
        "deletePerson".equals(methodName);
  }

  static boolean a() {
    return true;
  }
}
