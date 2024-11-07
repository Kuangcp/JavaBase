package com.github.kuangcp.serialize;

import lombok.Data;

import java.io.Serializable;

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

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
