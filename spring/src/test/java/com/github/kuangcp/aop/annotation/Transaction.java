package com.github.kuangcp.aop.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Aspect
@Component("transaction")
public class Transaction {

  @Pointcut("execution(* com.github.kuangcp.aop.annotation.PersonDaoImpl.*(..))")
  private void aopConfig() {
  }//方法签名  返回值必须是void 方法的修饰符最好是private

  @Before("aopConfig()")
  public void beginTransaction() {
    log.info("begin transaction");
  }
}
