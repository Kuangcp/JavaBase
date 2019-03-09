package com.github.kuangcp.reflects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:50
 * 使用反射得到对象的 成员属性
 * https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
 *
 * @author kuangcp
 */
@Slf4j
public class ReflectTargetObjectTest {

  @Test
  public void testModifyFinalPrimitive() throws ReflectiveOperationException {
    ReflectTargetObject target = new ReflectTargetObject();
    int targetValue = 6;
    Field field = ReflectTargetObject.class.getDeclaredField("no");
    field.setAccessible(true);

    field.set(target, targetValue);
    // TODO 仍然是旧值
    System.out.println(target.getNo());

    assertThat(field.get(target), equalTo(targetValue));
  }

  @Test
  public void testModifyPrimitive() throws ReflectiveOperationException {
    ReflectTargetObject target = new ReflectTargetObject();
    int targetValue = 6;
    Field field = ReflectTargetObject.class.getDeclaredField("num");
    field.setAccessible(true);

    Object value = field.get(target);
    log.info("value={}", value);

    field.set(target, targetValue);
    value = field.get(target);
    log.info(": value={} {}", value, target.getNum());
    assertThat(value, equalTo(targetValue));
    assertThat(target.getNum(), equalTo(targetValue));
  }

  @Test
  public void testModifyStatic() throws ReflectiveOperationException {
    String targetValue = "modifyFinalValue";
    Field field = ReflectTargetObject.class.getDeclaredField("type");
    field.setAccessible(true);

    Object value = field.get(ReflectTargetObject.class);
    log.info("static field: value={}", value);

    field.set(ReflectTargetObject.class, targetValue);

    assertThat(field.get(ReflectTargetObject.class), equalTo(targetValue));
    assertThat(ReflectTargetObject.type, equalTo(targetValue));
  }

  @Test
  public void testGetFinal() throws ReflectiveOperationException {
    Field id = ReflectTargetObject.class.getDeclaredField("id");
    id.setAccessible(true);
    Object value = id.get(new ReflectTargetObject());
    log.info("value={}", value);
  }

  @Test
  public void testModifyFinal() throws ReflectiveOperationException {
    String targetValue = "modifyFinalValue";
    Field field = ReflectTargetObject.class.getDeclaredField("id");
    field.setAccessible(true);
    ReflectTargetObject reflectDomain = new ReflectTargetObject();

    Object value = field.get(reflectDomain);
    log.info("value={}", value);
    log.info("domain={}", reflectDomain);

    field.set(reflectDomain, targetValue);

    value = field.get(reflectDomain);
    log.info("modify success: value={}", value);

    // TODO  改的是 field 的值, 原对象没有修改?
    log.info("domain={}", reflectDomain);

    assertThat(reflectDomain.id, equalTo(targetValue));
  }

  @Test(expected = IllegalAccessException.class)
  public void testModifyStaticFinal() throws ReflectiveOperationException {
    Field id = ReflectTargetObject.class.getDeclaredField("uid");
    id.setAccessible(true);

    Object value = id.get(ReflectTargetObject.class);
    log.info("value={}", value);

    // error: IllegalAccessException
    id.set(ReflectTargetObject.class, "modify");

    value = id.get(ReflectTargetObject.class);
    log.info("value={}", value);
  }

  @Test
  public void testModifyStaticFinalTwo() throws Exception {
    Field field = ReflectTargetObject.class.getDeclaredField("uid");
    field.setAccessible(true);

    Object value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);

    // TODO 不能像修改Boolean一样的修改
    removeFinalModifier(field, "modifySuccess");

    value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);
  }

  @Test
  public void testModifyStaticFinalBoolean() throws Exception {
    removeFinalModifier(Boolean.class.getField("FALSE"), true);
    log.info("Everything is {}", false);

    assertThat(false, equalTo(true));
  }

  // 移除 final 修饰符, 将属性变成仅 static 修饰
  private static void removeFinalModifier(Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(null, newValue);
  }

  /**
   * 获取所有定义的方法并执行get方法得到数据
   */
  @Test
  public void testInvokeMethodToGetFieldValue() throws ReflectiveOperationException {
    ReflectTargetObject domain = new ReflectTargetObject("name");
    Method[] methods = ReflectTargetObject.class.getDeclaredMethods();
    for (Method method : methods) {
      String methodName = method.getName();
      log.debug("method {}()", methodName);

      if (!methodName.startsWith("get")) {
        continue;
      }

      Object result = method.invoke(domain);
      if (Objects.equals(methodName, "getName")) {
        assertThat(result, equalTo("name"));
      }
      if (Objects.equals(methodName, "getAge")) {
        assertThat(result, equalTo(10L));
      }
    }
  }

  /**
   * 获取多参数的构造器
   */
  @Test
  public void testInvokeConstructor() throws ReflectiveOperationException {
    Class<ReflectTargetObject> domainClass = ReflectTargetObject.class;
    Constructor<ReflectTargetObject> constructor = domainClass.getConstructor(String.class);

    String name = "byConstructor";

    ReflectTargetObject domain = constructor.newInstance(name);
    log.info("use constructor:{}", domain.toString());

    assertThat(domain.getName(), equalTo(name));
  }
}
