package com.github.kuangcp;

/**
 * Created by https://github.com/kuangcp on 17-8-21  下午2:41
 * List常见用法
 */
public class FinalPoolLearn {

    public static void main(String[]s ){
        testInteger();
    }

    //java中的基本类型的包装类、其中 Byte、Boolean、Short、Character、Integer、Long 实现了常量池技术
    // （除了Boolean，都只对小于128的值才支持）
    private static void testInteger(){
//        对于-128~127的Integer对象才会到IntegerCache里获取缓存，使用常量池技术
        Integer i1 = 100;
        Integer i2 = 100;
        // 上面两行代码，使用自动装箱特性，编译成
        // Integer i1 = Integer.valueOf(100);
        // Integer i2 = Integer.valueOf(100);
        System.out.println(i1 == i2);

        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println(i3 == i4);
    }
}
