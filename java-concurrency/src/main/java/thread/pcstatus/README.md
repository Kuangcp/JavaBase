# 生产者消费者模型
- 理解wait notify的使用
    - 存在线程并发的情况,然后想通过wait和notify来控制线程的等待和运行从而达到按顺序来执行.
        - 这样的运行是会抛异常的 IllegalMonitorStateException 
        - 原因在于Share类中调用share对象的wait()方法时，不在同步方法或同步代码块中，因而当前线程并没有Share对象的锁，不能调用wait()方法。 
- 总结:
    - TODO 