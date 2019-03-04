package com.github.kuangcp.valatile
/**
 * Created by https://github.com/kuangcp
 * 指令重排问题
 * @author kuangcp
 * @date 18-4-2  上午9:20
 */
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