package com.github.kuangcp.queue

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.TransferQueue

/**
 * Created by https://github.com/kuangcp on 17-8-18  上午10:44
 * 当上游线程比下游线程快，这里就是生产者比消费者快，队列就会忙而等待 而且还会导致队列溢出
 *      反之，队列因此经常空着处于阻塞状态
 *
 */
class Element{
    String name

    Element(String name) {
        this.name = name
    }

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                '}';
    }
}
abstract  class MainThread extends Thread{
    final BlockingQueue<Element> elements
//    final TransferQueue<Element> elements
    String text = ""
    final int pauseTime
    boolean shutdown = false

    MainThread(BlockingQueue<Element> elements1, int pause){
        elements = elements1
        pauseTime = pause
    }
    synchronized shutdown(){

        shutdown = true
        println(text+" Stop "+shutdown)
    }

    @Override
    void run() {
        while (!shutdown){
            doAction()
            println("执行完特定方法"+shutdown)
            try{
                sleep(pauseTime)
            }catch (InterruptedException ignored){
                shutdown = true
            }
        }
    }
    // 具体实现由子类去确定，可以用来做生产者或者消费者
    abstract void doAction()
}

// 创建固定容量的队列
final BlockingQueue<Element> queue = new LinkedBlockingDeque<>(30)
//final TransferQueue<Element> queue = new LinkedTransferQueue<>()

MainThread proceduer = new MainThread(queue, 1000) {
    @Override
    void doAction() {
        text = "生产者"
        Element element = new Element("1")
        boolean handle = false
        try{
            // 添加进去，有超时控制
            handle = queue.offer(element, 100, TimeUnit.MILLISECONDS)
//            handle = queue.tryTransfer(element, 100, TimeUnit.MILLISECONDS)
            println("    放入")
        }catch (InterruptedException ignored){

        }
        if (!handle){
            println("Unable to hand off element to queue due to timeout")
        }
    }
}

MainThread consumer = new MainThread(queue, 100) {
    @Override
    void doAction() {
        text = "消费者"
        Element ele = null
        try{
            ele = queue.take()
            println("取出")
        }catch (InterruptedException ignored){
            println("?")
        }
    }
}

proceduer.start()
consumer.start()
// 睡眠不用catch，隐性的抛出异常？
sleep(5000)

// 如果消费者速度快于生产者，这个队列空着会阻塞主线程，可是这俩线程都停止了怎么还。。难以理解
// 生产者快于消费者是没有问题的
proceduer.shutdown()
consumer.shutdown()