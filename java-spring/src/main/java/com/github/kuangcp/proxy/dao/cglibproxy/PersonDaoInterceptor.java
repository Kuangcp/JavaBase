package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.base.Transaction;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class PersonDaoInterceptor implements MethodInterceptor {

  private Transaction transaction;
  private Object target;

  public PersonDaoInterceptor(Transaction transaction, Object target) {
    this.transaction = transaction;
    this.target = target;
  }

  public Object createProxy() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(this.target.getClass());//设置目标类为代理类的父类
    enhancer.setCallback(this);//设置拦截器为回调函数
    return enhancer.create();
  }


  @Override
  public Object intercept(Object arg0, Method method, Object[] arg2,
      MethodProxy arg3) throws Throwable {
    Object obj;
    String methodName = method.getName();
    if ("savePerson".equals(methodName) ||
        "updatePerson".equals(methodName) ||
        "deletePerson".equals(methodName)) {
      this.transaction.beginTransaction();
      obj = method.invoke(this.target, arg2);//调用目标类的目标方法
      this.transaction.commit();
    } else {
      obj = method.invoke(this.target, arg2);//调用目标类的目标方法
    }
    return obj;
  }

}
