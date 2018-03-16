package com.github.kuangcp.pcstatus;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:02
 * 参考于  http://blog.163.com/fan_yishan/blog/static/47692213200881981424126/
 * @author kuangcp
 */
public class Main {
    public static void main (String[] args) {
        Share s=new Share();
        Producer p=new Producer(s,1);
        Consumer c=new Consumer(s,1);
//        Producer p2=new Producer(s,2);
//        Consumer c2=new Consumer(s,2);
        p.start();
//        p2.start();
        c.start();
//        c2.start();
    }
}
