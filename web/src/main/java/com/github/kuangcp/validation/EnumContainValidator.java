package com.github.kuangcp.validation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumContainValidator implements ConstraintValidator<Contain, Object> {

  private Class<?> enumClass;

  @Override
  public void initialize(Contain constraintAnnotation) {
    this.enumClass = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(Object judgeValue, ConstraintValidatorContext constraintValidatorContext) {
    try {
      return Arrays.stream(enumClass.getFields())
          .filter(v -> Modifier.isFinal(v.getModifiers()) && Modifier.isStatic(v.getModifiers()))
          .anyMatch(v -> Objects.equals(getStaticFieldValue(v), judgeValue));
    } catch (Exception e) {
      log.error("", e);
    }

    return false;
  }

  public static Object getStaticFieldValue(Field field) {
    return getFieldValue(null, field);
  }

  public static Object getFieldValue(Object obj, Field field) {
    if (null == field) {
      return null;
    }
    if (obj instanceof Class) {
      // 静态字段获取时对象为null
      obj = null;
    }

    setAccessible(field);
    Object result;
    try {
      result = field.get(obj);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("IllegalAccess for " + field.getDeclaringClass() + field.getName(),
          e);
    }
    return result;
  }

  /**
   * 设置方法为可访问（私有方法可以被外部调用）
   *
   * @param <T> AccessibleObject的子类，比如Class、Method、Field等
   * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
   * @return 被设置可访问的对象
   * @since 4.6.8
   */
  public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
    if (null != accessibleObject && !accessibleObject.isAccessible()) {
      accessibleObject.setAccessible(true);
    }
    return accessibleObject;
  }
}
