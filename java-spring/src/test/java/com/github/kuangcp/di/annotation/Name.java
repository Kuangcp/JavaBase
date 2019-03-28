package com.github.kuangcp.di.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解描述了作用范围
 *   java             RetentionPolicy.SOURCE
 *   java+class       RetentionPolicy.CLASS
 *   java+class+jvm   RetentionPolicy.RUNTIME
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * 该注解既能在类上也能在方法上出现
 * @author Administrator
 *
 */
@Target({ElementType.TYPE})
@Documented//是否在帮助文档中出现
public @interface Name {
	String value() default "";//Name注解有一个属性为value
}
