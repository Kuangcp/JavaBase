package com.github.kuangcp.reflects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:50
 * 使用反射得到对象的 成员属性
 *
 * @author kuangcp
 */
@Slf4j
public class ObtainFieldsTest {

  // 不能获取到final属性的值
  @Test
  public void testGetFinal() throws NoSuchFieldException, IllegalAccessException {
    Field id = ReflectDomain.class.getDeclaredField("id");
    id.setAccessible(true);
    Object value = id.get(new ReflectDomain());
    log.info("value={}", value);
  }

  @Test
  public void testModifyFinal() throws NoSuchFieldException, IllegalAccessException {
    Field id = ReflectDomain.class.getDeclaredField("id");
    id.setAccessible(true);

    ReflectDomain reflectDomain = new ReflectDomain();
    Object value = id.get(reflectDomain);
    log.info("value={}", value);

    id.set(reflectDomain, "modify");
    value = id.get(reflectDomain);
    log.info("value={}", value);
  }

  @Test
  public void testModifyStaticFinal() throws NoSuchFieldException, IllegalAccessException {
    Field id = ReflectDomain.class.getDeclaredField("uid");
    id.setAccessible(true);

    Object value = id.get(ReflectDomain.class);
    log.info("value={}", value);

    // error: can not set
    id.set(ReflectDomain.class, "modify");
    value = id.get(ReflectDomain.class);
    log.info("value={}", value);
  }

  @Test
  public void testModifyStaticFinalTwo() throws NoSuchFieldException, IllegalAccessException {
    Field id = ReflectDomain.class.getDeclaredField("uid");
    id.setAccessible(true);

    Object value = id.get(ReflectDomain.class);
    log.info("value={}", value);

    // 去除 final 修饰符
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    int modifierValue = modifiersField.getInt(id);
    log.info("modifier={}", modifierValue);
    modifiersField.setInt(id, id.getModifiers() & ~Modifier.FINAL);
    modifiersField.setInt(id, id.getModifiers() & ~Modifier.STATIC);
    modifierValue = modifiersField.getInt(id);
    log.info("modifier={}", modifierValue);

    // error: can not set
    id.set(ReflectDomain.class, "modify");
    value = id.get(ReflectDomain.class);
    log.info("value={}", value);
  }

  /**
   * 获取所有定义的方法并执行get方法得到数据
   */
  @Test
  public void testObtainField() {
    ReflectDomain domain = new ReflectDomain("name", 10L);

    Method[] methods = ReflectDomain.class.getDeclaredMethods();
    for (Method method : methods) {
      log.debug("method {}()", method.getName());

      if (method.getName().startsWith("get")) {
        try {
          Object result = method.invoke(domain);
          log.info("invoke {}(): result={}", method.getName(), result.toString());
        } catch (ReflectiveOperationException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  /**
   * 获取多参数的构造器
   */
  @Test
  public void testInvokeConstructor() {
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
