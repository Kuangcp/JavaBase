/**
 * 演示两种
 * 如果通过  继承Thread类      来开发线程
 * 实现Runnable接口    来开发线程
 */
package thread.tryone;

import lombok.extern.slf4j.Slf4j;

public class Thread1 {

    public static void main(String[] args) {

        //创建一个Cat对象
        Cat cat = new Cat();
        //启动线程
        cat.start();

        //实现接口的方式来启动线程
        Dog dog = new Dog();
        Thread t = new Thread(dog);
        t.start();

    }

}

@Slf4j
//继承线程类
class Cat extends Thread {
    int times = 0;

    public void run() {
        while (true) {
            //休眠一秒 ，线程就会进入Blocked状态，并释放资源
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }
            times++;
            System.out.println("Cat hello world ！" + times);
            if (times == 10) {
                break;//这里跳出while循环，就会结束这个线程
            }
        }
    }
}

@Slf4j
//实现接口
class Dog implements Runnable {

    int times = 0;

    public void run() {
        while (true) {
            //休眠一秒 ，线程就会进入Blocked状态，并释放资源
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }
            times++;
            System.out.println("Dog Hello World ！" + times);
            if (times == 10) {
                break;//这里跳出while循环，就会结束这个线程
            }
        }
    }

}