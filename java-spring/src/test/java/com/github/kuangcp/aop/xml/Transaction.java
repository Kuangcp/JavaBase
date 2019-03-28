package com.github.kuangcp.aop.xml;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class Transaction {

  /**
   * 前置通知
   * 通过JoinPoint获取连接点的信息
   */
  public void beginTransaction(JoinPoint joinPoint) {
    joinPoint.getArgs();//获取方法的参数
    String methodName = joinPoint.getSignature().getName();
    System.out.println(methodName);
    System.out.println("begin transaction");
  }

  /**
   * 后置通知
   */
  public void commit(JoinPoint joinPoint, Object val) {
    List<Person> personList = (List<Person>) val;
    System.out.println(personList.size());
    System.out.println("commit");
  }

  /**
   * 最终通知
   */
  public void finallyMethod() {
    System.out.println("finally method");
  }

  /**
   * 异常通知
   */
  public void exceptionMethod(Throwable ex) {
    System.out.println(ex.getMessage());
  }

  /**
   * 环绕通知
   * 能控制目标方法的执行
   */
  public void aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("aaaa");
    String methodName = joinPoint.getSignature().getName();
    if ("savePerson".equals(methodName)) {
      joinPoint.proceed();
    }
  }
}
