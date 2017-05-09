package com.lambda.firstdemo;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class Main {
    public static void main(String []s){
        PointArrayList pointArrayList = new PointArrayList();
        pointArrayList.add(new Point(1,1));

        // 实现内部迭代

        pointArrayList.forEach(new TranslateByOne());
        for (Point point :pointArrayList) {
            System.out.println(point.toString());
        }

        // 使用java8的 Consumer 的accept方法 （针对于接口单方法的一个抽象接口）

        pointArrayList.add(new Point(5,5));
        pointArrayList.forEach(new TranslateByOne8());
        for (Point point :pointArrayList) {
            System.out.println("Java8\t "+point.toString());
        }

        //匿名内部类形式

        pointArrayList.forEach(new Consumer<Point>() {
            @Override
            public void accept(Point point) {
                point.translate(1,1);
            }
        });

        //lambda 表达式

        pointArrayList.forEach((Consumer<Point>) point -> point.translate(1,1));
        // 将lambda参数列表映射为假想的方法的参数列表
        // 裁剪了匿名内部类定义的额外代码，实现lambda
        for (Point point :pointArrayList) {
            System.out.println("lambda\t "+point.toString());
        }

    }
}
