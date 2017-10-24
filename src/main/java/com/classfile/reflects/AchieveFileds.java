package com.classfile.reflects;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:50
 * 使用反射得到对象的各种属性
 * @author kuangcp
 */
public class AchieveFileds {

    public static void main(String[] s){
        multiConstructor();
        Domain domain = new Domain();
        domain.setName("dfdfdf");
        domain.setAge(90L);
        declareMethodList(domain);
    }

    /**
     * 获取所有定义的方法并执行get方法得到数据
     */
    public static void declareMethodList(Domain object){
        Class domain = Domain.class;
        Method[] methods = domain.getDeclaredMethods();
        for (Method method : methods){
            System.out.println(method.getName());
            if (method.getName().startsWith("get")){
                try {
                    Object result = method.invoke(object);
                    System.out.println("    "+result.toString());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 获取多参数的构造器
     */
    private static void multiConstructor(){
        Class domain = Domain.class;
        try {
            Constructor constructor = domain.getConstructor(String.class, Long.class);
            Domain domain1 = (Domain) constructor.newInstance("909090",7878L);
            System.out.println(domain1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
