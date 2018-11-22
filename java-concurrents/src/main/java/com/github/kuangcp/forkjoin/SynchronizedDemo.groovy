package com.github.kuangcp.forkjoin

/**
 * Created by https://github.com/kuangcp
 * @author kuangcp* @date 18-4-1  下午2:54
 */

//status = 10
//while ((s = status) >= 0){
//    sleep(500)
//    status--
//    synchronized (this) {
//        if (status >= 0) {
//            try {
//                wait(0L);
//            } catch (InterruptedException ie) {
//                interrupted = true;
//            }
//        }
//        else
//            notifyAll();
//    }
//}

Counter a = new Counter()
a.setSize(89)
new Thread() {

  @Override
  void run() {
    for (int i = 0; i < 10; i++) {
      sleep(200)
      a.increase()
    }
  }
}.start()

new Thread() {

  @Override
  void run() {
    for (int i = 0; i < 10; i++) {
      sleep(100)
      print(a.current())
    }
  }
}.start()

print("<" + a.size + ">")
sleep(3000)
print("|||" + a.size)