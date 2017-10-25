package com.classfile.myth_serialize;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午3:35
 *
 * @author kuangcp
 */
public class Myth {
    private String name;
    private String phone;
    private String a;
    private String b;
    private String c;
    private Long test;

    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Myth{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", test=" + test +
                '}';
    }
}
