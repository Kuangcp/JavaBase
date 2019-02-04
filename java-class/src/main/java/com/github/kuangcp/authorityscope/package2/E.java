package com.github.kuangcp.authorityscope.package2;

import com.github.kuangcp.authorityscope.package1.A;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:33
 * 是A的子类但是不是同包
 * 总结：不同包的子类是可以访问protected的，
 */
public class E extends A {
    public static void main(String[] args){
        E jk = new E();
        //如果实例化A，就只能访问public
        // A jk = new A();
//		 A jks = new D();//public
//		D jk = (D)jks; //类型强转public + protected

        System.out.println(jk.v1);
        System.out.println(jk.v2);//通过Set get 可以得到
//		 System.out.println(jk.v3);//通过Set get 可以得到
        // System.out.println(jk.v4);
    }

}