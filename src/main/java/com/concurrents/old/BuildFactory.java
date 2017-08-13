package com.concurrents.old;

import groovy.transform.ToString;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午8:05
 *
 */
interface ObjBuilder<T>{
    T build();
}
//@ToString
class Update {
    private final String name;
    private final String addr;

    public Update(Builder builder) {
        this.name = builder.name;
        this.addr = builder.addr;
    }

    @Override
    public String toString() {
        return "Update{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    // 内部静态类
    public static class Builder implements ObjBuilder<Update>{
        private String name;
        private String addr;
        public Builder name(String name_){
            name = name_;
            return this;
        }
        public Builder addr(String add){
            addr = add;
            return this;
        }

        @Override
        public Update build() {
            return new Update(this);
        }
        // 内部类是可以多级嵌套的
        public static class d{
            public class g{

            }
        }
    }
}

public class BuildFactory{
    public static void main(String []s){
//        使用构建器模式来构建对象，使用了内部静态类
        Update.Builder ub = new Update.Builder();
        Update u = ub.name("sd").addr("12").build();
        System.out.println(u.toString());
    }
}
