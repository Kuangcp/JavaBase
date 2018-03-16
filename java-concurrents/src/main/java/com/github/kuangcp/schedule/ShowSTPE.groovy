package com.github.kuangcp.schedule

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午5:09
 * STPE 每隔500毫秒唤醒一个线程， 尝试poll一个队列，
 *      若任务队列为空就什么也不做，
 *      若执行cancel方法就会进入阻塞状态不会运行工作单元，直到又进行调用执行方法
 *      若得到工作单元，则线程就会执行工作单元
 */
class ReadService{
    // 消息队列
    BlockingQueue lbp = new LinkedBlockingQueue<>()
    // 一个延迟的、结果可接受的操作，可将其取消。通常已安排的 future 是用 ScheduledExecutorService 安排任务的结果。
    ScheduledFuture hndl
    ScheduledExecutorService stpe = Executors.newScheduledThreadPool(2)

    // 初始化一些信息,同样的调用了这个 线程池，执行了添加信息的工作单元
    void init(){
        lbp.add("初始化")
        lbp.add("1")
        lbp.add("2")
        lbp.add("3")
        final Runnable Reader = new Runnable() {
            @Override
            void run() {
                lbp.add(System.currentTimeMillis()+"")
            }
        }
        hndl = stpe.scheduleAtFixedRate(Reader, 10, 1000, TimeUnit.MILLISECONDS)
    }
    void run(){
        final Runnable Reader = new Runnable() {
            @Override
            void run() {
                String msg = lbp.poll().toString()
                if (msg != null){
                    println("Receive msg:"+msg)
                }

            }
        }
        hndl = stpe.scheduleAtFixedRate(Reader, 10, 500, TimeUnit.MILLISECONDS)
    }

    void cancel(){
        final ScheduledFuture handle = hndl
        stpe.schedule(new Runnable(){
            public void run(){
                handle.cancel(true)
            }
        }, 10, TimeUnit.MILLISECONDS)
    }
}

// run 和 cancel 是有顺序的，因为ScheduledFuture 对象共享覆盖的原因
// 如果 run run cancel cancel 就只会取消第二个run，第一个一直在运行
r = new ReadService()
r.init()
r.run()
sleep(3000)
r.cancel()
println("第二次")
r.run()
sleep(5000)
r.cancel()
