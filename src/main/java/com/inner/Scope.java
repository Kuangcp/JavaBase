package com.inner;

/**
 * Created by https://github.com/kuangcp on 18-3-7  下午10:53
 * 内部类以及外部类的一些变量的域
 * @author kuangcp
 */
public class Scope {



    public static void test(int a){

        class Inner {
            public int b=1;
            public int init(int c){
                return a+b+c;
            }
        }

        Inner in = new Inner();
        System.out.println(in.init(2));

    }

    public static void main(String[] a){
        Scope.test(1);
    }

}
