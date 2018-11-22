package com.github.kuangcp.sync

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService

/**
 *
 * @author kuangcp on 18-11-22-下午6:05
 */
class Cache {

  volatile ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>()
  volatile boolean flag = false

  Cache() {
    map.put("12", "22")
  }
/**
 * 进行操作, 原则上是 close之后, 就不允许执行这个方法了, 那么多线程共享的map变量就需要修饰一下了
 */
  void doOther() {
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

  void close(ExecutorService fixedThreadPool) {
// 如果没有在锁代码块中再次判断 90进程 :直接对 this 加锁 大约是 1-10个 和对map加锁基本一致

// 如果使用volatile关键字修饰标识变量, 也做不到保证一次, 因为只是保证了并发读, 并发写不能保证
// 使用锁加上双重判断就不会有问题
    synchronized (this) {
      if (map.size() == 0 || flag) {
        return
      }
      map.remove("12")
      flag = true
      println("关闭")
//            fixedThreadPool.awaitTermination()
    }
  }
}