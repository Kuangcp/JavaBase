package com.classfile.reflects;

/**
 * Created by https://github.com/kuangcp on 17-10-24  上午9:48
 * 反射操作的对象
 * @author kuangcp
 */
public class Domain {
    String name;
    Long age;

    public Domain(){}
    public Domain(String name, Long age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
