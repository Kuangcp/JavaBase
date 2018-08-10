package com.github.kuangcp.old

import java.util.concurrent.CountDownLatch

/**
 * Created by https://github.com/kuangcp on 17-8-16  上午8:57
 * 1 构建器模式 实例化对象
 * 2 锁存器的使用
 *      countDown 只是为了减数，当0就唤醒所有的线程
 *      await 判断，为0不操作，不为0 就休眠当前语句所处线程
 * 终于理解了写的案例类 ShowCopyOnWrite 里的语句运行逻辑了
 */
// 1
//BuildFactory.Builder ub = new BuildFactory.Builder();
//BuildFactory u = ub.name("sd").addr("12").build();
//System.out.println(u.getCount());

// 2
firstLatch = new CountDownLatch(1)
secondLatch = new CountDownLatch(1)

def runs() {
  Thread.start {
    println("进入 线程 1")
//        secondLatch.await()
    println("1 前" + firstLatch.getCount())
    // 只是将计数器减一，如果减后的数值是0， 就唤醒所有的等待线程
    firstLatch.countDown()

    // 参数用的是第二个锁存器的参数，但是休眠的是这个线程，当前线程
    // 因为线程2 还没有减1 所以这里是必然的要休眠，  直到线程2 的countDown运行，这里才唤醒
    secondLatch.await()

    println("1 后 " + firstLatch.getCount() + ":" + secondLatch.getCount())
    println("线程 1 ")
  }
  Thread.start {
//        Thread.sleep(5000)
    println("线程2 休眠1 前 ：" + firstLatch.getCount())

    firstLatch.await()

    println("进入 线程 2")
//        secondLatch.await()
    println("2 前" + secondLatch.getCount())

    // 唤醒了休眠的线程 1
    // 所以还是不能确保线程的顺序，应该要将 目标代码运行完后，再进行唤醒，线程的随机性太大，这样稳妥些
    secondLatch.countDown()
//        Thread.sleep(1000)
    println("2 后 ${secondLatch.getCount()}")
    println("线程 2")

  }
}

runs()