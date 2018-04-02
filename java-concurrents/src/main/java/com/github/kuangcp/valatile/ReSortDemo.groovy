package com.github.kuangcp.valatile

import java.util.concurrent.TimeUnit

/**
 * Created by https://github.com/kuangcp
 * 指令重排问题
 * @author kuangcp
 * @date 18-4-2  上午9:20
 */

// 这种单例模式  https://www.oschina.net/question/2611757_2194452
// DCL 写法
class Person{
    Person(){}
    private static Person instance = null
    static Person getInstance(){
        if(instance == null){
            synchronized (Person.class){
                if(instance == null){
                    instance = new Person()
                }
            }
        }
        return instance
    }
}
class ReSortDemo{
    volatile static boolean stop

    static void main(String[]s){
//        demo1()
        demo2()
    }

    static void demo2(){
        new Thread(){
            @Override
            void run(){
                Person person
                for (int i = 0; i < 5; i++) {
                    person = Person.getInstance()
                    println(person)
                    if(person != Person.getInstance()){
                        println("不止一个实例")
                    }
                }
            }
        }.start()
        new Thread(){
            @Override
            void run(){
                Person person
                for (int i = 0; i < 3; i++) {
                    person = Person.getInstance()
                    println(person)
                    if(person != Person.getInstance()){
                        println("不止一个实例")
                    }
                }
            }
        }.start()
    }
    static void demo1(){
        Thread workThread = new Thread(new Runnable() {
            @Override
            void run() {
                int i = 0
                while(!stop){
                    i++
                    try{
                        TimeUnit.SECONDS.sleep(1)
                    }catch (InterruptedException e){
                        e.printStackTrace()
                    }
                    println("i"+i)
                }
            }
        })
        workThread.start()
        TimeUnit.SECONDS.sleep(3)
        stop = true
    }
}

