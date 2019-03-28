package com.github.kuangcp.aop.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component("transaction")
@Aspect
public class Transaction {

  @Pointcut("execution(* com.github.kuangcp.aop.annotation.PersonDaoImpl.*(..))")
  private void aa() {
  }//方法签名  返回值必须是void 方法的修饰符最好是private

  @Before("aa()")
  public void beginTransaction() {
    System.out.println("begin transaction");
  }
}
