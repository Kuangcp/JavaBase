package com.concurrents.schedule

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午5:09
 * STPE 每隔10毫秒唤醒一个线程， 尝试poll一个队列，如果队列为空就什么也不做，得到工作单元，则线程就会输出工作单元的内容
 */
class ReadService{
    BlockingQueue lbp = new LinkedBlockingQueue<>()
    ScheduledFuture hdnl
    ScheduledExecutorService stpe

    void run(){
        stpe = Executors.newScheduledThreadPool(2)
        final Runnable Reader = new Runnable() {
            @Override
            void run() {
                String msg = lbp.poll().toString()
                if (msg != null){
                    println("Receive msg:"+msg)
                }

            }
        }
        hdnl = stpe.scheduleAtFixedRate(Reader, 10, 10, TimeUnit.MILLISECONDS)
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

