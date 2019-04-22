package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.base.Transaction;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 感觉像是织入器 将增强和切点类作为属性
 *
 * @author Myth
 */
public class PersonDaoInterceptor implements InvocationHandler {

  private Transaction transaction;
  private Object target;

  public PersonDaoInterceptor(Transaction transaction, Object target) {
    this.transaction = transaction;
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object obj;
    String methodName = method.getName();
    //对指定的方法增强
    if ("savePerson".equals(methodName) ||
        "updatePerson".equals(methodName) ||
        "deletePerson".equals(methodName)) {
      this.transaction.beginTransaction();
      obj = method.invoke(this.target, args);//调用目标类的目标方法
      this.transaction.commit();
    } else {
      obj = method.invoke(this.target, args);//调用目标类的目标方法
    }
    return obj;
  }

}
