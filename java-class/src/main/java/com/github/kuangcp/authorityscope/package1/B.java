package com.github.kuangcp.authorityscope.package1;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:32
 * 同包下：除了private 都能访问
 */
public class B {

    public static void main(String[] args){
        A jk = new A();
        System.out.println(jk.v1);
        System.out.println(jk.v2);
        System.out.println(jk.v3);
//		System.out.println(jk.v4);
    }
}
