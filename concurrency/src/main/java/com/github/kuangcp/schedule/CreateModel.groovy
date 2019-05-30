package com.github.kuangcp.schedule

import java.util.concurrent.*

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午3:59
 * 任务建模的三种方法 Callable Future FutureTask
 * Callable 是SAM类型（Single Abstract Method） 的示例， 这是Java把函数作为一等类型的可行方法
 * Future接口 用来表示异步任务，
 *      - get() 用来获取结果，如果结果还没准备好就会阻塞直到它能去到结果，有一个可以设置超时的版本，这个版本永远不会阻塞
 *      - cancel() 运算结束前取消
 *      - isDone() 调用者用它来判断运算是否结束
 */

list = new ArrayList<>()
list.add(12)
list.add(134)

Callable<String> callable = new Callable<String>() {

  @Override
  String call() throws Exception {
    return list.toString()
  }
}
println("result = " + callable.call())

def executor
try {
  executor = Executors.newSingleThreadExecutor()
  Future<String> future = executor.submit(callable)

  def result = future.get(60, TimeUnit.MILLISECONDS)
  println("future: " + result)
} catch (TimeoutException e) {
  println("timeout" + e)
} finally {
  if (executor != null) {
    executor.shutdown()
  }
}



