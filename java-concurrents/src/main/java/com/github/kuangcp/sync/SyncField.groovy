package com.github.kuangcp.sync

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by https://github.com/kuangcp
 * @author kuangcp
 * TODO 如何保证多线程并发情况下, 关闭一执行, 后面的操作全都不能做
 *  现在做到了只关闭一次, 如何判断是否阻塞
 * @date 18-4-17  上午10:16
 */
class Cache{
    volatile ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>()
    volatile boolean flag = false

    Cache(){
        map.put("12", "22")
    }
    /**
     * 进行操作, 原则上是 close之后, 就不允许执行这个方法了, 那么多线程共享的map变量就需要修饰一下了
     */
    void doOther(){
        // 这里如果 使用this或者map效果一样, 那么就是对对象加锁,多线程要依次等待锁释放, 所以执行次数和循环调用次数不一致
        if (map.size() == 0 || flag) {
            return
        }
        int d = map.size()
        synchronized (this) {
            if (map.size() == 0 || flag) {
                return
            }
            int sum = 0
            for (int i = 0; i < 100; i++) {
                sum *= i
                sum -= i
            }
            println(System.currentTimeMillis() + " 计算 " + sum + " | " + map.size())
        }
    }
    void close(ExecutorService fixedThreadPool){
        // 如果没有在锁代码块中再次判断 90进程 :直接对 this 加锁 大约是 1-10个 和对map加锁基本一致

        // 如果使用volatile关键字修饰标识变量, 也做不到保证一次, 因为只是保证了并发读, 并发写不能保证
        // 使用锁加上双重判断就不会有问题
        synchronized (this) {
            if(map.size() == 0 || flag){
                return
            }
            map.remove("12")
            flag = true
            println("关闭")
//            fixedThreadPool.awaitTermination()
        }
    }
}
class SyncField {
    static void main(String[]s){
        Cache cache = new Cache()
        int num = 50
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(90)
        for (int i = 1; i <= num; i++) {
            fixedThreadPool.execute(new Runnable() {
                @Override
                void run() {
                    for(;;) {
                        if (cache.map.size() == 0 || cache.flag) {
                            return
                        }
                        int result = (int) ((Math.random() * 100) / 10 + 1)
                        println(result)
                        if ( result < 2) {
                            println("准备关闭")
                            cache.close(fixedThreadPool)
                        } else {
                            cache.doOther()
                        }
                        sleep(500)
                    }
                }
            })
        }
        fixedThreadPool.shutdown()
    }
}


