package com.github.kuangcp.old;

import lombok.Data;

/**
 * 构造者模式，相当于 Builder 注解
 */
@Data
public class BuildFactory {

  private final String name;
  private final String addr;

  private BuildFactory(Builder builder) {
    this.name = builder.name;
    this.addr = builder.addr;
  }

  // 内部静态类
  public static class Builder {

    private String name;
    private String addr;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder addr(String add) {
      addr = add;
      return this;
    }

    public BuildFactory build() {
      return new BuildFactory(this);
    }
  }
}