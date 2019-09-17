package com.github.kuangcp.proxy.salary.aop.xml;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

@Slf4j
public class Transaction {

  /**
   * 前置通知
   * 通过JoinPoint获取连接点的信息
   */
  public void beginTransaction(JoinPoint joinPoint) {
    joinPoint.getArgs();//获取方法的参数
    String methodName = joinPoint.getSignature().getName();
    log.info(methodName);
    log.info("begin transaction");
  }

  /**
   * 后置通知
   */
  public void commit(JoinPoint joinPoint, Object val) {
    List<Person> personList = (List<Person>) val;
    log.info("size={}", personList.size());
    log.info("commit");
  }

  /**
   * 最终通知
   */
  public void finallyMethod() {
    log.info("finally method");
  }

  /**
   * 异常通知
   */
  public void exceptionMethod(Throwable ex) {
    log.info(ex.getMessage());
  }

  /**
   * 环绕通知  能控制目标方法的执行
   */
  public void aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("before proceed {}", joinPoint.getSignature().getName());
    String methodName = joinPoint.getSignature().getName();
    if ("savePerson".equals(methodName)) {
      joinPoint.proceed();
    }
		log.info("after proceed {}", joinPoint.getSignature().getName());
  }
}
