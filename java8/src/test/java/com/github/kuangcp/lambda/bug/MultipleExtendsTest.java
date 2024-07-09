package com.github.kuangcp.lambda.bug;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-11-28 19:43
 */
public class MultipleExtendsTest {

    // 普通写法
    @Test
    public void testMap() {
        List<C> list = Arrays.asList(new CImpl(), new CImpl());
        List<String> names = list.stream().map(A::getName).collect(Collectors.toList());
        System.out.println(names);
    }

    private static <E extends A & B> long sum(List<E> list) {
        return list.stream().map(E::getName).count() + list.stream().map(E::getNum).count();
    }

    // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8142476
    // 旧版本JDK 1.8 运行时会报错，但是 1.8.402 也没报错了
    @Test
    public void testMapWithGeneric() {
        List<C> list = Arrays.asList(new CImpl(), new CImpl());
        System.out.println(sum(list));
        System.out.println(sum(Arrays.asList(new Combine(), new Combine())));
    }
}


interface A {

    String getName();
}

interface B {

    Integer getNum();
}

interface C extends A, B {

}

class CImpl implements C {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getNum() {
        return null;
    }
}

class Combine implements A, B {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getNum() {
        return null;
    }
}