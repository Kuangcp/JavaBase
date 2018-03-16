package com.github.kuangcp.pcstatus;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:01
 *
 * @author kuangcp
 */
public class Producer extends Thread{
    private Share shared;
    private int number;

    public Producer(Share s,int number){
        shared=s;
        this.number=number;
    }

    public void run(){
        for(int i=0;i<10;i++){
            shared.put(i);
            System.out.println("生产者"+this.number+" 输出的数据为:"+i);
            try{
                sleep((int)(Math.random()*100));
            }catch(InterruptedException e){}
        }
    }
}
