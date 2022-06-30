package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.common.InterceptorLogic;
import com.github.kuangcp.proxy.dao.common.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态增强，织入事务控制
 *
 * @author Myth
 */
@Slf4j
@AllArgsConstructor
public class TransactionInterceptor implements InvocationHandler {

  private final Transaction transaction;
  private final Object target;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.debug("method={} proxy={}", method.getName(), proxy);

    return InterceptorLogic.transactionLogic(transaction, target, method, args);
  }
}
