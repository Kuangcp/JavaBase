package com.github.kuangcp.sync

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by https://github.com/kuangcp
 * @author kuangcp* TODO 如何保证多线程并发情况下, 关闭一执行, 后面的操作全都不能做
 *  现在做到了只关闭一次, 如何判断是否阻塞
 * @date 18-4-17  上午10:16
 */

class SyncField {

  static void main(String[] s) {
    Cache cache = new Cache()
    int num = 50
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(90)
    for (int i = 1; i <= num; i++) {
      fixedThreadPool.execute(new Runnable() {

        @Override
        void run() {
          for (; ;) {
            if (cache.map.size() == 0 || cache.flag) {
              return
            }
            int result = (int) ((Math.random() * 100) / 10 + 1)
            println(result)
            if (result < 2) {
              println("准备关闭")
              cache.close(fixedThreadPool)
            } else {
              cache.doOther()
            }
            sleep(500)
          }
        }
      })
    }
    fixedThreadPool.shutdown()
  }
}


