package com.github.kuangcp.pcstatus;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:02
 * 生产者和消费者之间的共享区，数据的交互都发生在这里
 *  这两个方法都要加上同步关键字 synchronized 不然就会报错
 *      原因在于Share类中调用share对象的wait()方法时，不在同步方法或同步代码块中，因而当前线程并没有Share对象的锁，不能调用wait()方法。
 * @author kuangcp
 */
public class Share{
    private int contents;// 共享的数据
    private boolean available=false;// 标志变量 是否有数据 可获取

    /**
     * 取数据
     *  如果有数据 设置不可获取,唤醒其他线程 然后返回数据
     *  如果没有 那就等待
     */
    public synchronized int get(){
        while(!available){
            try{
                wait();//每个对象都有这个方法,所以是调用当前对象的wait方法 将当前对象等待
            }catch(InterruptedException ignored){}
        }
        available=false;// 语句先后不影响,因为状态变量没有改变,其他方法也不会乱执行.
        notifyAll();
        return contents;
    }

    /**
     * 放数据
     *  同步锁,如果已经有数据就等待
     *  如果没有数据就放数据进去,然后唤醒其他线程
     */
    public synchronized void put(int value){
        while(available){
            try{
                wait();
            }catch(InterruptedException ignored){}
        }
        available=true;
        notifyAll();
        contents=value;
    }
}
