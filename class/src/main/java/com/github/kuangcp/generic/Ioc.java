package com.github.kuangcp.generic;


import com.github.kuangcp.generic.common.Human;
import com.github.kuangcp.generic.common.Student;

/**
 *
 * 2023-12-13 16:26
 */
public class Ioc {

    /**
     * 如果返回不做类型强转，无法编译通过
     * 思考：泛型T在编译期拥有了多态性，不确定。
     */
    public <T extends Human> Class<T> getClz(int code) {
        if (code == 1) {
            return (Class<T>) Student.class;
        } else if (code == 2) {
            return (Class<T>) Human.class;
        }
        return null;
    }

    /**
     * 直接使用通配符是正常
     */
    public Class<? extends Human> getClass(int code) {
        if (code == 1) {
            return Student.class;
        } else if (code == 2) {
            return Human.class;
        }
        return null;
    }
}
