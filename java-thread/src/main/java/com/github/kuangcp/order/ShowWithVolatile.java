package com.github.kuangcp.order;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:57
 * 使用了volatile关键字。让每个线程都能拿到最新的count的值，当其中一个线程执行++操作后，
 * 其他两个线程就会拿到最新的值，并检查是否符合准入条件。
 *
 * volatile不是线程安全的。而且两者没有任何关系。volatile变量不在用户线程保存副本，因此对所有线程都能提供最新的值。
 *       但试想，如果多个线程同时并发更新这个变量，其结果也是显而易见的，最后一次的更新会覆盖前面所有更新,从而导致线程不安全。
 *       该示例一次只有一个线程满足准入条件，因此不存在对变量的并发更新。
 *
 *    volatile(易失性)的值是最新的与线程安全完全是不相干的，所以不要误用volatile实现并发控制。
 *  相关博客 http://blog.csdn.net/yuechang5/article/details/79081697
 * @author kuangcp
 */
public class ShowWithVolatile implements Runnable{
    String target;
    Holder holder;
    int order;

    public ShowWithVolatile(String target, int order, Holder holder) {
        this.holder = holder;
        this.order = order;
        this.target = target;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (holder.count % 5 == order) {
                System.out.println(Thread.currentThread().getName() + " ===== "+ order);
                holder.count ++;
            }
        }
// int i = 0;
// while(i ++ < 10000){
//  holder.count ++;
// }
    }
}
