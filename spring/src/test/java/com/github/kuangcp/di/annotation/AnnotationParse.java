package com.github.kuangcp.di.annotation;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * 注解解析器
 * @author Administrator
 *
 */
public class AnnotationParse {
	public static void parse(){
		Class classt = ITCAST.class;
		/**
		 * 类上的注解
		 */
		if(classt.isAnnotationPresent(Name.class)){
			Name name = (Name)classt.getAnnotation(Name.class);
			System.out.println(name.value());
		}
		
		Method[] methods = classt.getMethods();
		for(Method method:methods){
			if(method.isAnnotationPresent(Description.class)){
				Description description = (Description)method.getAnnotation(Description.class);
				System.out.println(description.value());
			}
		}
	}
	
	@Test
	public void test(){
		AnnotationParse.parse();
	}
}
