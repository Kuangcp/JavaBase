package com.github.kuangcp.proxy.salary.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 1、把日志、安全性框架、权限导入进去
 * 2、把目标类导入进去
 * 3、上述两类通过构造函数赋值
 *
 * @author Administrator
 */
@Slf4j
public class SalaryInterceptor implements InvocationHandler {

  private List<Interceptor> interceptorList;

  private Object target;

  public SalaryInterceptor(Object target, List<Interceptor> interceptorList) {
    this.target = target;
    this.interceptorList = interceptorList;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    // 执行所有的切面中的通知
    for (Interceptor interceptor : interceptorList) {
      interceptor.interceptor();
    }
    log.debug("invoke real method");
    method.invoke(this.target, args);
    return null;
  }
}
