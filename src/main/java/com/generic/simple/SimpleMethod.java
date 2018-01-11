package com.generic.simple;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:44
 *
 * @author kuangcp
 */
public class SimpleMethod {
    /**
     * 简单的泛型方法 可以取代掉以往的Object的工具类的方法
     * @param list 对象集合
     * @param <T> 类型必须是对象, 即使是基本数据类型,也要使用对应的包装类
     * @return 返回中间的对象
     */
    public static <T> T getMiddle(T[] list){
        return list[list.length/2];
    }
}
