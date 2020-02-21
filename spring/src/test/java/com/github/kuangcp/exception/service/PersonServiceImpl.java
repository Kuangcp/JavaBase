package com.github.kuangcp.exception.service;

import com.github.kuangcp.exception.ServiceMapping;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * 接口的实现类，提供了反射实现某方法的具体实现
 * 之后的类要继承，通过ServiceInvocation类来调用，从而执行指定类和方法
 * 如果不继承的话 就至少要实现service接口又要重写service方法（假如重写的方法不是这样写，就不会执行指定方法了）
 *
 * @author Myth
 */
@Slf4j
public class PersonServiceImpl implements Service {

  @Override
  public Object service(ServiceMapping serviceMapping) throws Exception {
    String methodName = serviceMapping.getMethod();
    log.info("方法是:" + methodName);
    Object obj = Class.forName(serviceMapping.getServiceClass()).newInstance();
    Method method = Class.forName(serviceMapping.getServiceClass()).getMethod(methodName);
    return method.invoke(obj);
  }
}
