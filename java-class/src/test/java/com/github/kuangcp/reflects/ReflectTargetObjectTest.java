package com.github.kuangcp.reflects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
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

    Field field = ReflectTargetObject.class.getDeclaredField("finalInt");
    field.setAccessible(true);

    field.set(target, targetValue);
    // TODO 仍然是旧值
    System.out.println(target.getFinalInt());

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

    log.info("static field: value={}", field.get(ReflectTargetObject.class));

    field.set(ReflectTargetObject.class, targetValue);

    assertThat(field.get(ReflectTargetObject.class), equalTo(targetValue));
    assertThat(ReflectTargetObject.type, equalTo(targetValue));
  }

  @Test
  public void testGetFinal() throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("finalString");
    field.setAccessible(true);
    Object value = field.get(new ReflectTargetObject());
    log.info("value={}", value);
  }

  /**
   * 修改 final String 属性 失败
   */
  @Test
  @Ignore
  public void testModifyFinalString() throws ReflectiveOperationException {
    String targetValue = "modifyFinalValue";
    ReflectTargetObject target = new ReflectTargetObject();

    Field field = ReflectTargetObject.class.getDeclaredField("finalString");
    field.setAccessible(true);

    log.info("before={} {}", field.get(target), target);
    field.set(target, targetValue);
    log.info("modify success: value={}", field.get(target));

    // TODO  改的是 field 的值, 原对象没有修改?
    //  但是 Debug窗口中看到的对象的属性的确改了 .finalString 是改了, .getFinalString() 则是没改 ???

    // 初步理解, final修饰后 无法更改引用的值(也就是地址值), String 又是不可变的, 有运行时常量池的存在, 所以原对象上的String类型属性是改不动的
    // 那么问题来了, field 能改动? Idea 中 Debug 取到的值也是改动后的

    log.info("domain={}", target);
    System.out.println(target.getFinalString().equals(targetValue));

    System.out.println(target.finalString);

    assertThat(target.finalString, equalTo(targetValue));
  }

  @Test
  public void testModifyFinalInteger() throws ReflectiveOperationException {
    int targetValue = 12;
    ReflectTargetObject target = new ReflectTargetObject();

    Field field = ReflectTargetObject.class.getDeclaredField("finalInteger");
    field.setAccessible(true);

    log.info("before={}", field.get(target));
    field.set(target, targetValue);
    assertThat(field.get(target), equalTo(targetValue));

    log.info("after={}", target.getFinalInteger());
    assertThat(target.getFinalInteger(), equalTo(targetValue));
  }

  /**
   * 修改 static final 属性
   */
  @Test
  @Ignore
  public void testModifyStaticFinal() throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalString");
    field.setAccessible(true);

    Object value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);

    //TODO error: IllegalAccessException
    field.set(ReflectTargetObject.class, "modify");

    value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);
  }

  @Test
  @Ignore
  public void testModifyStaticFinalTwo() throws Exception {
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalString");

    field.setAccessible(true);
    Object value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);

    // TODO ERROR 不能像修改Boolean一样的修改
    removeFinalModifier(field, "modifySuccess");

    value = field.get(ReflectTargetObject.class);
    log.info("value={}", value);
  }

  @Test
  public void testModifyStaticFinalBoolean() throws Exception {
    removeFinalModifier(Boolean.class.getField("FALSE"), true);

    log.info("Everything is true? {}", false);
    assertThat(false, equalTo(true));
  }

  /**
   * 移除 static final 修饰的属性上的 final 修饰符
   */
  private static void removeFinalModifier(Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // static 传入 null
    field.set(null, newValue);
  }

  /**
   * 获取所有定义的方法并执行get方法得到数据
   */
  @Test
  public void testInvokeMethodToGetFieldValue() throws ReflectiveOperationException {
    ReflectTargetObject target = new ReflectTargetObject("name");
    Method[] methods = ReflectTargetObject.class.getDeclaredMethods();
    for (Method method : methods) {
      String methodName = method.getName();
      log.debug("method {}()", methodName);

      if (!methodName.startsWith("get")) {
        continue;
      }

      Object result = method.invoke(target);
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
