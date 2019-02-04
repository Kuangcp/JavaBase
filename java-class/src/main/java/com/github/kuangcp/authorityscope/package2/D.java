package com.github.kuangcp.authorityscope.package2;

import com.github.kuangcp.authorityscope.package1.A;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:32
 * 不同包的访问 只能访问public
 */
public class D {

    public static void main(String[] args){
        A jk = new A();
        System.out.println(jk.v1);
        // System.out.println(jk.v2);
        // System.out.println(jk.v3);
        // System.out.println(jk.v4);
    }
}
