package com.github.kuangcp.lambda.firstdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.function.Consumer;

@Slf4j
public class MainTest {

    private final ElementContainer<Point> pointArrayList = new ElementContainer<>();

    @Before
    public void init() {
        pointArrayList.add(new Point(1, 1));
        pointArrayList.add(new Point(5, 5));
    }

    // 1. 利用内部迭代, 将一个接口实现类的对象传入
    @Test
    public void testByPolymorphic() {
        pointArrayList.forEach(new TranslateByOne());

        for (Point point : pointArrayList) {
            log.info("for each " + point.toString());
        }
    }

    // 2. 利用 Java8 的函数式接口 Consumer 的accept方法 （针对于接口单方法的一个抽象接口）
    @Test
    public void testConsumer() {
        pointArrayList.forEach(new TranslateByOne8());
        for (Point point : pointArrayList) {
            log.info("Java8\t " + point.toString());
        }
    }

    // 3. 将上面的 TranslateByOne8 接口 以匿名内部类形式实现
    @Test
    public void testAnonymousInnerClass() {
        pointArrayList.forEach(new Consumer<Point>() {
            @Override
            public void accept(Point point) {
                point.translate(1, 1);
                log.info("AnonymousInnerClass " + point);
            }
        });
    }

    // 4. lambda 表达式
    @Test
    public void testLambda() {
//    pointArrayList.forEach((Consumer<Point>) point -> point.translate(1, 1));
        pointArrayList.forEach((ElementAction) point -> point.translate(2, 1));

        // 将lambda参数列表映射为假想的方法的参数列表
        // 裁剪了匿名内部类定义的额外代码，实现lambda
        for (Point point : pointArrayList) {
            log.info("lambda\t " + point.toString());
        }
    }
}

