package com.github.kuangcp.valatile

/**
 * Created by https://github.com/kuangcp
 * 多线程并发写和读
 *  怎么避免这种问题 ?
 *  TODO 解决方案
 * @author kuangcp
 * @date 18-4-13  下午4:48
 */
class RepeatRead {
    volatile boolean close

    void start(String thread){
        if(!isClose()){
            return
        }
        setClose(false)
        println("启动"+thread)
    }
    void close(String thread){
        if(isClose()){
            return
        }
        setClose(true)
        println("进行关闭"+thread)
    }
}

class Main{
    static void main(String[] s){
        RepeatRead read = new RepeatRead()
        new Thread(new Runnable() {
            @Override
            void run() {
                println('线程1')
                read.start('线程1')
                sleep(300)
                read.close('线程1')
            }
        }).start()
        new Thread(new Runnable() {
            @Override
            void run() {
                println('线程2')
                read.start('线程1')
                sleep(1200)
                read.close('线程2')
            }
        }).start()
        new Thread(new Runnable() {
            @Override
            void run() {
                println('线程3')
                read.start('线程1')
                sleep(1300)
                read.close('线程3')
            }
        }).start()
    }
}

