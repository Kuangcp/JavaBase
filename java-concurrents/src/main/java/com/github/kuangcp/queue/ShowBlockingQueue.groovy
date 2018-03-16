package com.github.kuangcp.queue

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by https://github.com/kuangcp on 17-8-18  上午9:28
 * 展示BlockingQueue的特性 多个兽医治疗宠物的队列
 */
// 因为父类没有无参构造器，所以子类也没有，子类也要显式声明
// 宠物类
abstract  class Pet{
    protected String name;
    public Pet(String name){
        this.name = name;
    }
    public abstract void examine();
}

class Cat extends Pet{
    public Cat(String name){
        super(name);
    }

    @Override
    void examine() {
        println("cat ："+name) //+"  addr:"+this
    }
}
class Dog extends Pet{

    Dog(String name) {
        super(name)
    }

    @Override
    void examine() {
        println("dog : "+name)
    }
}
// 接待类,所有的宠物实例先达到这里再给兽医
class Appointmnet<T>{
    private final T toBeSeen;
    public T getPatient(){
//        println("接待： "+toBeSeen.toString())
        return toBeSeen;
    }
    public Appointmnet(T incoming){
        toBeSeen = incoming;
    }
}

class Veterinarian extends Thread{
    protected final BlockingQueue<Appointmnet<Pet>> appts;
    protected String text = "";
    protected final int restTime; // 预约之间的休息时间
    private boolean shutdown = false;
    public Veterinarian(BlockingQueue<Appointmnet<Pet>> lists, int pause){
        appts = lists;
        restTime = pause;
    }
    public synchronized void shutdown(){
        shutdown = true;
    }

    @Override
    void run() {
        while (!shutdown){
            seePatient();
            try{
                sleep(restTime);
            }catch (InterruptedException ignored){
                shutdown = true;
            }
        }
    }
    /**
     * 线程从队列中取出预约， 进行操作，如果当前队列没有预约， 就会阻塞
     */
    public void seePatient(){
        try{
            Appointmnet<Pet> ap = appts.take(); // 阻塞take
            Pet patient = ap.getPatient();
            println(text+" take "+patient.name)
            patient.examine();
        }catch(InterruptedException ignored){
            shutdown = true;
        }
    }
}
BlockingQueue<Appointmnet<Pet>> lists = new LinkedBlockingQueue<Appointmnet<Pet>>()
app = new Appointmnet<Pet>(new Cat("name"))
lists.add(app)
//app.patient.name = "update"
//lists.add(app)
lists.add(new Appointmnet<Pet>(new Cat("name2")))


veter = new Veterinarian(lists, 2000);
veter.text = "1"
veter.start()
// 链表这种对象，实参形参内存共享，所以可以启动后添加
lists.add(new Appointmnet<Pet>(new Dog("add")))
lists.add(new Appointmnet<Pet>(new Dog("dog2")))
// 队列为空就阻塞了

veter2 = new Veterinarian(lists, 1000);
veter2.text = "2"
veter2.start()
