package com.threads;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:44
 * 创建线程的多种方式
 */

/**
 * 继承方式来实现线程
 */
class ExampleOne extends Thread{
    boolean runFlag = true;
    @Override
    public void run() {
        while(runFlag) {
            System.out.println("继承方式来实现线程 ");
        }
    }
}

/**
 * 实现接口方式来实现线程
 */
class ExampleTwo implements Runnable{
    boolean runFlag = true;
    @Override
    public void run() {
        while(runFlag){
            System.out.println("实现接口方式来实现线程");
        }
    }
}

/**
 * 直接实例化一个匿名内部方法在方法体里
 */
class ExampleThree{
    void target(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("直接实例化一个匿名内部方法在方法体里");
            }
        }).start();
    }
}
public class HowToCreateThread {
}
