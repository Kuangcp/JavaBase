package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.common.InterceptorLogic;
import com.github.kuangcp.proxy.dao.common.Transaction;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Slf4j
@AllArgsConstructor
public class TransactionInterceptor implements MethodInterceptor {

  private Transaction transaction;
  private Object target;

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
    log.info("method {} proxy object ={}", method.getName(), proxy);

    return InterceptorLogic.transactionLogic(transaction, target, method, args);
  }
}
