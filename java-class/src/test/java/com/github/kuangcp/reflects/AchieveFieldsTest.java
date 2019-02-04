package com.github.kuangcp.reflects;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:50
 * 使用反射得到对象的 成员属性
 *
 * @author kuangcp
 */
@Slf4j
public class AchieveFieldsTest {

  /**
   * 获取所有定义的方法并执行get方法得到数据
   */
  @Test
  public void invokeAllGetMethod() {
    ReflectDomain domain = new ReflectDomain();
    domain.setName("name");
    domain.setAge(90L);

    Class<ReflectDomain> domainClass = ReflectDomain.class;
    Method[] methods = domainClass.getDeclaredMethods();
    for (Method method : methods) {
      log.info("method: : methodName={}", method.getName());

      if (method.getName().startsWith("get")) {
        Object result;
        try {
          result = method.invoke(domain);
          log.info("invoke getMethod: result={}", result.toString());
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 获取多参数的构造器
   */
  @Test
  public void invokeMultiConstructor() {
    Class<ReflectDomain> domainClass = ReflectDomain.class;
    try {
      Constructor<ReflectDomain> constructor = domainClass.getConstructor(String.class, Long.class);
      ReflectDomain domain = constructor.newInstance("909090", 7878L);
      log.info("use constructor:{}", domain.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
