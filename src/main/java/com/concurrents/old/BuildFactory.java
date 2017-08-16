package com.concurrents.old;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午8:05
 * 不可变对象以及构建器
 *
 */
interface ObjBuilder<T>{
    T build();
}

public class BuildFactory {
    private final String name;
    private final String addr;

    public BuildFactory(Builder builder) {
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
    public static class Builder implements ObjBuilder<BuildFactory>{
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
        public BuildFactory build() {
            return new BuildFactory(this);
        }
        // 内部类是可以多级嵌套的
        public static class d{
            public class g{

            }
        }
    }
}


