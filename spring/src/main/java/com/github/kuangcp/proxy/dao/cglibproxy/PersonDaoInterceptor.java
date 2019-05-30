package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.base.CustomInterceptor;
import com.github.kuangcp.proxy.dao.base.Transaction;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class PersonDaoInterceptor implements MethodInterceptor, CustomInterceptor {

  private Transaction transaction;
  private Object target;

  public PersonDaoInterceptor(Transaction transaction, Object target) {
    this.transaction = transaction;
    this.target = target;
  }

  // cglib 方式就是动态创建一个子类
  public Object createProxy() {
    Enhancer enhancer = new Enhancer();
    //设置代理类为目标类的子类
    enhancer.setSuperclass(this.target.getClass());
    //设置拦截器为回调函数
    enhancer.setCallback(this);
    return enhancer.create();
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
      throws Throwable {
    Object result;
    if (isNeedTransaction(method.getName())) {
      this.transaction.beginTransaction();
      result = method.invoke(this.target, method);//调用目标类的目标方法
      this.transaction.commit();
    } else {
      result = method.invoke(this.target, method);//调用目标类的目标方法
    }
    return result;
  }
}
