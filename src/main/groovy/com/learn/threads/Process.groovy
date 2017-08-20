package com.learn.threads

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by https://github.com/kuangcp on 17-8-15  下午5:30
 * Groovy 关于线程的学习：
 *
 * 多线程调用 集合的获取迭代器的方法时，会由于同步问题，运行出不是预期的结果，例如得到相同的迭代器对象
 *      加上 synchronized 就可以避免，那这样使用并发包有何意义
 * 原来是要搭配上锁存器使用.就能严格控制线程的运行顺序逻辑
 *
 */

def threadStart(){
    // 开启线程的三种方式
    Thread.startDaemon {
        // 这个是挂在后台做守护线程？
        println("1")
    }
    Thread.start {
        println("2")
    }
    t = new Thread({
        print("3")
    })
    t.start()

}

def multiProcess(){
    CopyOnWriteArrayList list = new CopyOnWriteArrayList()
    list.add("1")
    list.add("2")
    list.add("3")
    println(list)
    for (i in [1, 2, 3, 4]){
        Thread.start {
            startRun(list)
        }
    }

}

/**
 * 启动一个线程运行，去获取迭代器 加上 synchronized 关键字就可以避免这种情况
 * @param list
 */
def  startRun(list){
    it = list.iterator()
    println("得到一个 "+it)

}

multiProcess()

