package com.github.kuangcp.block;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午9:44
 * 用锁存器辅助初始化：
 *      这里把锁存器的初始值设置为 quorum .一旦达到这个数量，就可以开始处理更新了，每个线程完成初始化后都会马上调用 countDown()
 *      主线程只要等 quorum 的达到，然后启动，再派发更新（这里没给出）
 */
public class UseCountLatch{


    public static void main(String []s){
        final int MAX_THREADS = 12;
        // 同一进程内的一组更新线程至少必须要有一般的线程正确初始化之后，才能开始接收系统发送给他们任何一个线程的更新
        final int quorum = 1 + (int)(MAX_THREADS/2);
        final CountDownLatch cdl = new CountDownLatch(quorum);

        final Set<ProcessingThread> nodes = new HashSet<>();
        try{
            for (int i=0; i< MAX_THREADS; i++){
                ProcessingThread local = new ProcessingThread("localhost"+(9000+i), cdl);
                nodes.add(local);
                local.start();
            }
            cdl.await(); //达到quorum 开始发送更新
        }catch (InterruptedException e){

        }finally {

        }
    }
}

class ProcessingThread extends Thread{
    private final String ids;
    private final CountDownLatch latch;

    public ProcessingThread(String id, CountDownLatch latch) {
        this.ids = id;
        this.latch = latch;
    }


    public String getIds() {
        return ids;
    }
    public void init(){
        // 节点初始化
        latch.countDown();
    }
    public void run(){
        init();
        System.out.println(this.ids);
    }
}

