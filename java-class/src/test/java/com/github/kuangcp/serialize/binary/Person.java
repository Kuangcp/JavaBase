package com.github.kuangcp.serialize.binary;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:26
 *
 * @author kuangcp
 */
@Data
public class Person implements Serializable {

  private String name;
  private String address;
  private String phone;

  @Override
  public String toString() {
    return "Person{" +
        "name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", phone='" + phone + '\'' +
        '}';
  }
}
