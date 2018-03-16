package com.github.kuangcp.pcstatus;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:02
 * 消费者
 * @author kuangcp
 */
public class Consumer extends Thread{
    private Share shared;
    private int number;

    public Consumer(Share s,int number){
        shared=s;
        this.number=number;
    }

    public void run(){
        int value;
        for(int i=0;i<10;i++){
            value=shared.get();
            System.out.println("消费者"+this.number+" 得到的数据为:"+value);
        }
    }
}
