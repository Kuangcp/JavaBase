package com.github.kuangcp.customserialize;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午3:35
 *
 * @author kuangcp
 */
public class Myth {
    private String name;
    private String phone;
    private Long test;

    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
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
                ", test=" + test +
                '}';
    }
}
