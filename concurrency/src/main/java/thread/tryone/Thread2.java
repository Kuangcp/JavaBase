package thread.tryone;

import lombok.extern.slf4j.Slf4j;

/**
 * 两个线程同时运作
 *
 * @author lenovo
 */
public class Thread2 {

    public static void main(String[] a) {
        T1 t1 = new T1(10);
        T2 t2 = new T2(10);

        Thread t = new Thread(t1);
        Thread s = new Thread(t2);

        t.start();
        s.start();
    }
}

@Slf4j
//最好是用实现接口来写  方便以后扩展
class T1 implements Runnable {

    //做累加的功能
    int n = 0;
    int res = 0;
    int times = 0;

    public T1(int n) {
        this.n = n;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

            } catch (Exception e) {
                log.error("", e);
            }
            res += (++times);
            System.out.println("当前结果是：" + res);
            if (times == n) {
                System.out.println("最后的结果是：" + res);
                break;
            }
        }
    }

}

@Slf4j
class T2 implements Runnable {

    int n = 0;
    int times = 0;

    public T2(int n) {
        this.n = n;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }

            times++;
            System.out.println("我是一个线程，在输出第" + times + "hello world");
            if (times == n) break;
        }
    }
}












