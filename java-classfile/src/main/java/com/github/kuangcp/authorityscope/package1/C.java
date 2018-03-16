package com.github.kuangcp.authorityscope.package1;

import com.github.kuangcp.authorityscope.package2.E;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:32
 */

//同一个包 并且是 子类
public class C extends A{

    public static void main(String[] args){
        C c = new C();
        System.out.println(c.v1);
        System.out.println(c.v2);
        System.out.println(c.v3);
        //System.out.println(jk.v4);
    }
}