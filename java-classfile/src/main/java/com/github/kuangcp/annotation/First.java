package com.github.kuangcp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by https://github.com/kuangcp on 17-10-25  下午3:01
 * 参考博客 http://www.cnblogs.com/xd502djj/archive/2012/07/26/2610040.html
 * 自定义注解：
 *
 * @author kuangcp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface First {
    public String  column() default "";
}
