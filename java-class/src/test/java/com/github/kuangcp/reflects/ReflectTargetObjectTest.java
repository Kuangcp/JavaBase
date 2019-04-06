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
 * http://www.cnblogs.com/noKing/p/9038234.html
 *
 * @author kuangcp
 */
@Slf4j
public class ReflectTargetObjectTest {

  @Test
  public void testGetFinal() throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("finalString");

    // 可有可无, 原本就是具有该属性的访问权限
    field.setAccessible(true);

    Object value = field.get(new ReflectTargetObject());
    log.info("value={}", value);
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

  @Test
  public void testModifyPrimitive() throws ReflectiveOperationException {
    ReflectTargetObject target = new ReflectTargetObject();
    int targetValue = 6;
    Field field = ReflectTargetObject.class.getDeclaredField("num");
    field.setAccessible(true);

    Object value = field.get(target);
    log.info("before={}", value);

    field.set(target, targetValue);
    value = field.get(target);
    log.info("after: {} {}", value, target.getNum());

    assertThat(value, equalTo(targetValue));
    assertThat(target.getNum(), equalTo(targetValue));
  }

  // 编译后字节码文件中的 字面量 会将所有引用常量的地方全替换成字面量, 基本类型和String都是一样的场景
  @Test
  public void testModifyFinalPrimitive() throws ReflectiveOperationException {
    ReflectTargetObject target = new ReflectTargetObject();
    int targetValue = 6;

    Field field = ReflectTargetObject.class.getDeclaredField("finalInt");
    field.setAccessible(true);
    field.set(target, targetValue);

    assertThat(field.get(target), equalTo(targetValue));
    // 如果在这里断点就会发现 field 和 target 对象里的值都已经被修改了, 但是这里的断言却能通过
    // 因为编译做了优化, 反编译字节码能看到:
    // assertThat(target.getFinalInt(), equalTo(1));
    // 并且 getFinalInt() 方法也是写死的返回 1 所以对象上的属性改了也没有用, 调用该属性的地方全被写死了
    assertThat(target.getFinalInt(), equalTo(target.finalInt));
  }

  /**
   * 修改 final String 属性
   */
  @Test
  public void testModifyFinalString() throws ReflectiveOperationException {
    String targetValue = "modifyFinalValue";
    ReflectTargetObject target = new ReflectTargetObject();

    Field field = ReflectTargetObject.class.getDeclaredField("finalString");
    field.setAccessible(true);

    log.info("before={} {}", field.get(target), target);
    field.set(target, targetValue);
    log.info("modify success: value={}", field.get(target));

    assertThat(target.finalString, equalTo(target.getFinalString()));
    assertThat(field.get(target), equalTo(targetValue));
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

  // 修改 static final 属性

  @Test
  @Ignore
  public void testModifyStaticFinalInteger() throws Exception {
    int targetValue = 10;
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalInteger");
    field.setAccessible(true);

    log.info("before={}", field.get(ReflectTargetObject.class));

    //TODO error: IllegalAccessException
    removeFinalModifier(field, targetValue);

    log.info("after={}", field.get(ReflectTargetObject.class));
  }

  @Test
  @Ignore
  public void testModifyStaticFinalString() throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalString");
    field.setAccessible(true);

    log.info("value={}", field.get(ReflectTargetObject.class));

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    //TODO error: IllegalAccessException
//    field.set(ReflectTargetObject.class, "modify");
    field.set(ReflectTargetObject.class, "modify");

    log.info("value={}", field.get(ReflectTargetObject.class));
  }

  public static void main(String[] args) throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalString");
    field.setAccessible(true);

    log.info("value={}", field.get(ReflectTargetObject.class));

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    //TODO error: IllegalAccessException
//    field.set(ReflectTargetObject.class, "modify");
    field.set(ReflectTargetObject.class, "modify");

    log.info("value={}", field.get(ReflectTargetObject.class));
  }

  //TODO  static 按照网上说法是可以改的, 但是就是不行, 换Java7 也不行  但是 Boolean 又能改
  // https://stackoverflow.com/questions/2474017/using-reflection-to-change-static-final-file-separatorchar-for-unit-testing#
  // https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html
  // https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
  @Test
  @Ignore
  public void testModifyStaticFinalStringThree() throws ReflectiveOperationException {
    Field field = ReflectTargetObject.class.getDeclaredField("staticFinalStringBuilder");
    field.setAccessible(true);

    log.info("value={}", field.get(ReflectTargetObject.class));

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    //TODO error: IllegalAccessException
//    field.set(ReflectTargetObject.class, "modify");
    field.set(null, new StringBuilder("modify"));

    log.info("value={}", field.get(ReflectTargetObject.class));
  }

  @Test
  @Ignore
  public void testModifyStaticFinalStringTwo() throws Exception {
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
}
