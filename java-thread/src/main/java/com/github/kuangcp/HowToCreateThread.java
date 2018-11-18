package com.github.kuangcp;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:44
 * 创建线程的多种方式
 */
public class HowToCreateThread {

  /**
   * 继承方式来实现线程
   * 继承Thread 重写run方法
   */
  class ExampleOne extends Thread {

    boolean runFlag = true;

    @Override
    public void run() {
      while (runFlag) {
        System.out.println("继承方式来实现线程 ");
      }
    }
  }

  /**
   * 实现接口方式来实现线程
   * Runnable不是线程，是线程要运行的代码的宿主。
   */
  class ExampleTwo implements Runnable {

    boolean runFlag = true;

    @Override
    public void run() {
      while (runFlag) {
        System.out.println("实现接口方式来实现线程");
      }
    }
  }

  /**
   * 直接实例化一个匿名内部方法在方法体里
   * 匿名内部类
   */
  class ExampleThree {

    void target() {
      // lambda方式
      new Thread(() -> {
        System.out.println("直接实例化一个匿名内部方法在方法体里");
        System.out.println("多行语句");
      }).start();

      new Thread(() -> System.out.println("只有一条语句")).start();

      // 普通方式
      new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println("直接实例化一个匿名内部方法在方法体里");
        }
      }).start();
    }
  }
}
