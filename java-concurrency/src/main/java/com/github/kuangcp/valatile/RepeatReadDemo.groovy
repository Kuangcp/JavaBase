package com.github.kuangcp.valatile

class RepeatReadDemo {

  // 输出没有 1 2 3
  static void main(String[] s) {
    RepeatRead read = new RepeatRead()
    new Thread(new Runnable() {
      @Override
      void run() {
        println('线程1')
        read.start('线程1')
        sleep(300)
        read.close('线程1')
      }
    }).start()

    new Thread(new Runnable() {
      @Override
      void run() {
        println('线程2')
        read.start('线程1')
        sleep(1200)
        read.close('线程2')
      }
    }).start()

    new Thread(new Runnable() {
      @Override
      void run() {
        println('线程3')
        read.start('线程1')
        sleep(1300)
        read.close('线程3')
      }
    }).start()
  }
}