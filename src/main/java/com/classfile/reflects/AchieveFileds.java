package com.classfile.reflects;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:50
 * 使用反射得到对象的各种属性
 * @author kuangcp
 */
public class AchieveFileds {

    public static void main(String[] s){
        multiConstructor();
    }

    public void declareMethodList(){

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
